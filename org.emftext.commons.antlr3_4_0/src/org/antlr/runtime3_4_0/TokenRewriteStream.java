/**
 * Copyright (C) 2014 SINTEF <franck.fleurey@sintef.no>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 [The "BSD license"]
 Copyright (c) 2005-2009 Terence Parr
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
     derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.antlr.runtime3_4_0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** Useful for dumping out the input stream after doing some
 *  augmentation or other manipulations.
 *
 *  You can insert stuff, replace, and delete chunks.  Note that the
 *  operations are done lazily--only if you convert the buffer to a
 *  String.  This is very efficient because you are not moving data around
 *  all the time.  As the buffer of tokens is converted to strings, the
 *  toString() method(s) check to see if there is an operation at the
 *  current index.  If so, the operation is done and then normal String
 *  rendering continues on the buffer.  This is like having multiple Turing
 *  machine instruction streams (programs) operating on a single input tape. :)
 *
 *  Since the operations are done lazily at toString-time, operations do not
 *  screw up the token index values.  That is, an insert operation at token
 *  index i does not change the index values for tokens i+1..n-1.
 *
 *  Because operations never actually alter the buffer, you may always get
 *  the original token stream back without undoing anything.  Since
 *  the instructions are queued up, you can easily simulate transactions and
 *  roll back any changes if there is an error just by removing instructions.
 *  For example,
 *
 *   CharStream input = new ANTLRFileStream("input");
 *   TLexer lex = new TLexer(input);
 *   TokenRewriteStream tokens = new TokenRewriteStream(lex);
 *   T parser = new T(tokens);
 *   parser.startRule();
 *
 * 	 Then in the rules, you can execute
 *      Token t,u;
 *      ...
 *      input.insertAfter(t, "text to put after t");}
 * 		input.insertAfter(u, "text after u");}
 * 		System.out.println(tokens.toString());
 *
 *  Actually, you have to cast the 'input' to a TokenRewriteStream. :(
 *
 *  You can also have multiple "instruction streams" and get multiple
 *  rewrites from a single pass over the input.  Just name the instruction
 *  streams and use that name again when printing the buffer.  This could be
 *  useful for generating a C file and also its header file--all from the
 *  same buffer:
 *
 *      tokens.insertAfter("pass1", t, "text to put after t");}
 * 		tokens.insertAfter("pass2", u, "text after u");}
 * 		System.out.println(tokens.toString("pass1"));
 * 		System.out.println(tokens.toString("pass2"));
 *
 *  If you don't use named rewrite streams, a "default" stream is used as
 *  the first example shows.
 */
public class TokenRewriteStream extends CommonTokenStream {
	public static final String DEFAULT_PROGRAM_NAME = "default";
    public static final int PROGRAM_INIT_SIZE = 100;
	public static final int MIN_TOKEN_INDEX = 0;

	// Define the rewrite operation hierarchy

	class RewriteOperation {
        /** What index into rewrites List are we? */
        protected int instructionIndex;
        /** Token buffer index. */
        protected int index;
		protected Object text;

		protected RewriteOperation(int index) {
			this.index = index;
		}

		protected RewriteOperation(int index, Object text) {
			this.index = index;
			this.text = text;
		}
		/** Execute the rewrite operation by possibly adding to the buffer.
		 *  Return the index of the next token to operate on.
		 */
		public int execute(StringBuffer buf) {
			return index;
		}
		public String toString() {
			String opName = getClass().getName();
			int $index = opName.indexOf('$');
			opName = opName.substring($index+1, opName.length());
			return "<"+opName+"@"+tokens.get(index)+
				   ":\""+text+"\">";
		}
	}

	class InsertBeforeOp extends RewriteOperation {
		public InsertBeforeOp(int index, Object text) {
			super(index,text);
		}
		public int execute(StringBuffer buf) {
			buf.append(text);
			if ( tokens.get(index).getType()!=Token.EOF ) {
				buf.append(tokens.get(index).getText());
			}
			return index+1;
		}
	}

	/** I'm going to try replacing range from x..y with (y-x)+1 ReplaceOp
	 *  instructions.
	 */
	class ReplaceOp extends RewriteOperation {
		protected int lastIndex;
		public ReplaceOp(int from, int to, Object text) {
			super(from,text);
			lastIndex = to;
		}
		public int execute(StringBuffer buf) {
			if ( text!=null ) {
				buf.append(text);
			}
			return lastIndex+1;
		}
		public String toString() {
			if ( text==null ) {
				return "<DeleteOp@"+tokens.get(index)+
					   ".."+tokens.get(lastIndex)+">";
			}
			return "<ReplaceOp@"+tokens.get(index)+
				   ".."+tokens.get(lastIndex)+":\""+text+"\">";
		}
	}

	/** You may have multiple, named streams of rewrite operations.
	 *  I'm calling these things "programs."
	 *  Maps String (name) -> rewrite (List)
	 */
	protected Map programs = null;

	/** Map String (program name) -> Integer index */
	protected Map lastRewriteTokenIndexes = null;

	public TokenRewriteStream() {
		init();
	}

	protected void init() {
		programs = new HashMap();
		programs.put(DEFAULT_PROGRAM_NAME, new ArrayList(PROGRAM_INIT_SIZE));
		lastRewriteTokenIndexes = new HashMap();
	}

	public TokenRewriteStream(TokenSource tokenSource) {
	    super(tokenSource);
		init();
	}

	public TokenRewriteStream(TokenSource tokenSource, int channel) {
		super(tokenSource, channel);
		init();
	}

	public void rollback(int instructionIndex) {
		rollback(DEFAULT_PROGRAM_NAME, instructionIndex);
	}

	/** Rollback the instruction stream for a program so that
	 *  the indicated instruction (via instructionIndex) is no
	 *  longer in the stream.  UNTESTED!
	 */
	public void rollback(String programName, int instructionIndex) {
		List is = (List)programs.get(programName);
		if ( is!=null ) {
			programs.put(programName, is.subList(MIN_TOKEN_INDEX,instructionIndex));
		}
	}

	public void deleteProgram() {
		deleteProgram(DEFAULT_PROGRAM_NAME);
	}

	/** Reset the program so that no instructions exist */
	public void deleteProgram(String programName) {
		rollback(programName, MIN_TOKEN_INDEX);
	}

	public void insertAfter(Token t, Object text) {
		insertAfter(DEFAULT_PROGRAM_NAME, t, text);
	}

	public void insertAfter(int index, Object text) {
		insertAfter(DEFAULT_PROGRAM_NAME, index, text);
	}

	public void insertAfter(String programName, Token t, Object text) {
		insertAfter(programName,t.getTokenIndex(), text);
	}

	public void insertAfter(String programName, int index, Object text) {
		// to insert after, just insert before next index (even if past end)
		insertBefore(programName,index+1, text);
	}

	public void insertBefore(Token t, Object text) {
		insertBefore(DEFAULT_PROGRAM_NAME, t, text);
	}

	public void insertBefore(int index, Object text) {
		insertBefore(DEFAULT_PROGRAM_NAME, index, text);
	}

	public void insertBefore(String programName, Token t, Object text) {
		insertBefore(programName, t.getTokenIndex(), text);
	}

	public void insertBefore(String programName, int index, Object text) {
		RewriteOperation op = new InsertBeforeOp(index,text);
		List rewrites = getProgram(programName);
        op.instructionIndex = rewrites.size();
        rewrites.add(op);		
	}

	public void replace(int index, Object text) {
		replace(DEFAULT_PROGRAM_NAME, index, index, text);
	}

	public void replace(int from, int to, Object text) {
		replace(DEFAULT_PROGRAM_NAME, from, to, text);
	}

	public void replace(Token indexT, Object text) {
		replace(DEFAULT_PROGRAM_NAME, indexT, indexT, text);
	}

	public void replace(Token from, Token to, Object text) {
		replace(DEFAULT_PROGRAM_NAME, from, to, text);
	}

	public void replace(String programName, int from, int to, Object text) {
		if ( from > to || from<0 || to<0 || to >= tokens.size() ) {
			throw new IllegalArgumentException("replace: range invalid: "+from+".."+to+"(size="+tokens.size()+")");
		}
		RewriteOperation op = new ReplaceOp(from, to, text);
		List rewrites = getProgram(programName);
        op.instructionIndex = rewrites.size();
        rewrites.add(op);
	}

	public void replace(String programName, Token from, Token to, Object text) {
		replace(programName,
				from.getTokenIndex(),
				to.getTokenIndex(),
				text);
	}

	public void delete(int index) {
		delete(DEFAULT_PROGRAM_NAME, index, index);
	}

	public void delete(int from, int to) {
		delete(DEFAULT_PROGRAM_NAME, from, to);
	}

	public void delete(Token indexT) {
		delete(DEFAULT_PROGRAM_NAME, indexT, indexT);
	}

	public void delete(Token from, Token to) {
		delete(DEFAULT_PROGRAM_NAME, from, to);
	}

	public void delete(String programName, int from, int to) {
		replace(programName,from,to,null);
	}

	public void delete(String programName, Token from, Token to) {
		replace(programName,from,to,null);
	}

	public int getLastRewriteTokenIndex() {
		return getLastRewriteTokenIndex(DEFAULT_PROGRAM_NAME);
	}

	protected int getLastRewriteTokenIndex(String programName) {
		Integer I = (Integer)lastRewriteTokenIndexes.get(programName);
		if ( I==null ) {
			return -1;
		}
		return I.intValue();
	}

	protected void setLastRewriteTokenIndex(String programName, int i) {
		lastRewriteTokenIndexes.put(programName, new Integer(i));
	}

	protected List getProgram(String name) {
		List is = (List)programs.get(name);
		if ( is==null ) {
			is = initializeProgram(name);
		}
		return is;
	}

	private List initializeProgram(String name) {
		List is = new ArrayList(PROGRAM_INIT_SIZE);
		programs.put(name, is);
		return is;
	}

	public String toOriginalString() {
        fill();
		return toOriginalString(MIN_TOKEN_INDEX, size()-1);
	}

	public String toOriginalString(int start, int end) {
		StringBuffer buf = new StringBuffer();
		for (int i=start; i>=MIN_TOKEN_INDEX && i<=end && i<tokens.size(); i++) {
			if ( get(i).getType()!=Token.EOF ) buf.append(get(i).getText());
		}
		return buf.toString();
	}

	public String toString() {
        fill();
		return toString(MIN_TOKEN_INDEX, size()-1);
	}

	public String toString(String programName) {
        fill();
		return toString(programName, MIN_TOKEN_INDEX, size()-1);
	}

	public String toString(int start, int end) {
		return toString(DEFAULT_PROGRAM_NAME, start, end);
	}

	public String toString(String programName, int start, int end) {
		List rewrites = (List)programs.get(programName);

        // ensure start/end are in range
        if ( end>tokens.size()-1 ) end = tokens.size()-1;
        if ( start<0 ) start = 0;

        if ( rewrites==null || rewrites.size()==0 ) {
			return toOriginalString(start,end); // no instructions to execute
		}
		StringBuffer buf = new StringBuffer();

		// First, optimize instruction stream
		Map indexToOp = reduceToSingleOperationPerIndex(rewrites);

        // Walk buffer, executing instructions and emitting tokens
        int i = start;
        while ( i <= end && i < tokens.size() ) {
			RewriteOperation op = (RewriteOperation)indexToOp.get(new Integer(i));
			indexToOp.remove(new Integer(i)); // remove so any left have index size-1
			Token t = (Token) tokens.get(i);
			if ( op==null ) {
				// no operation at that index, just dump token
				if ( t.getType()!=Token.EOF ) buf.append(t.getText());
				i++; // move to next token
			}
			else {
				i = op.execute(buf); // execute operation and skip
			}
		}

        // include stuff after end if it's last index in buffer
        // So, if they did an insertAfter(lastValidIndex, "foo"), include
        // foo if end==lastValidIndex.
        if ( end==tokens.size()-1 ) {
            // Scan any remaining operations after last token
            // should be included (they will be inserts).
            Iterator it = indexToOp.values().iterator();
            while (it.hasNext()) {
                RewriteOperation op = (RewriteOperation)it.next();
                if ( op.index >= tokens.size()-1 ) buf.append(op.text);
            }
        }
        return buf.toString();
	}

	/** We need to combine operations and report invalid operations (like
	 *  overlapping replaces that are not completed nested).  Inserts to
	 *  same index need to be combined etc...   Here are the cases:
	 *
	 *  I.i.u I.j.v								leave alone, nonoverlapping
	 *  I.i.u I.i.v								combine: Iivu
	 *
	 *  R.i-j.u R.x-y.v	| i-j in x-y			delete first R
	 *  R.i-j.u R.i-j.v							delete first R
	 *  R.i-j.u R.x-y.v	| x-y in i-j			ERROR
	 *  R.i-j.u R.x-y.v	| boundaries overlap	ERROR
	 *
	 *  Delete special case of replace (text==null):
	 *  D.i-j.u D.x-y.v	| boundaries overlap	combine to max(min)..max(right)
	 *
	 *  I.i.u R.x-y.v | i in (x+1)-y			delete I (since insert before
	 *											we're not deleting i)
	 *  I.i.u R.x-y.v | i not in (x+1)-y		leave alone, nonoverlapping
	 *  R.x-y.v I.i.u | i in x-y				ERROR
	 *  R.x-y.v I.x.u 							R.x-y.uv (combine, delete I)
	 *  R.x-y.v I.i.u | i not in x-y			leave alone, nonoverlapping
	 *
	 *  I.i.u = insert u before op @ index i
	 *  R.x-y.u = replace x-y indexed tokens with u
	 *
	 *  First we need to examine replaces.  For any replace op:
	 *
	 * 		1. wipe out any insertions before op within that range.
	 *		2. Drop any replace op before that is contained completely within
	 *         that range.
	 *		3. Throw exception upon boundary overlap with any previous replace.
	 *
	 *  Then we can deal with inserts:
	 *
	 * 		1. for any inserts to same index, combine even if not adjacent.
	 * 		2. for any prior replace with same left boundary, combine this
	 *         insert with replace and delete this replace.
	 * 		3. throw exception if index in same range as previous replace
	 *
	 *  Don't actually delete; make op null in list. Easier to walk list.
	 *  Later we can throw as we add to index -> op map.
	 *
	 *  Note that I.2 R.2-2 will wipe out I.2 even though, technically, the
	 *  inserted stuff would be before the replace range.  But, if you
	 *  add tokens in front of a method body '{' and then delete the method
	 *  body, I think the stuff before the '{' you added should disappear too.
	 *
	 *  Return a map from token index to operation.
	 */
	protected Map reduceToSingleOperationPerIndex(List rewrites) {
//		System.out.println("rewrites="+rewrites);

		// WALK REPLACES
		for (int i = 0; i < rewrites.size(); i++) {
			RewriteOperation op = (RewriteOperation)rewrites.get(i);
			if ( op==null ) continue;
			if ( !(op instanceof ReplaceOp) ) continue;
			ReplaceOp rop = (ReplaceOp)rewrites.get(i);
			// Wipe prior inserts within range
			List inserts = getKindOfOps(rewrites, InsertBeforeOp.class, i);
			for (int j = 0; j < inserts.size(); j++) {
				InsertBeforeOp iop = (InsertBeforeOp) inserts.get(j);
				if ( iop.index == rop.index ) {
					// E.g., insert before 2, delete 2..2; update replace
					// text to include insert before, kill insert
					rewrites.set(iop.instructionIndex, null);
					rop.text = iop.text.toString() + (rop.text!=null?rop.text.toString():"");
				}
				else if ( iop.index > rop.index && iop.index <= rop.lastIndex ) {
                    // delete insert as it's a no-op.
                    rewrites.set(iop.instructionIndex, null);
				}
			}
			// Drop any prior replaces contained within
			List prevReplaces = getKindOfOps(rewrites, ReplaceOp.class, i);
			for (int j = 0; j < prevReplaces.size(); j++) {
				ReplaceOp prevRop = (ReplaceOp) prevReplaces.get(j);
				if ( prevRop.index>=rop.index && prevRop.lastIndex <= rop.lastIndex ) {
                    // delete replace as it's a no-op.
                    rewrites.set(prevRop.instructionIndex, null);
					continue;
				}
				// throw exception unless disjoint or identical
				boolean disjoint =
					prevRop.lastIndex<rop.index || prevRop.index > rop.lastIndex;
				boolean same =
					prevRop.index==rop.index && prevRop.lastIndex==rop.lastIndex;
				// Delete special case of replace (text==null):
				// D.i-j.u D.x-y.v	| boundaries overlap	combine to max(min)..max(right)
				if ( prevRop.text==null && rop.text==null && !disjoint ) {
					//System.out.println("overlapping deletes: "+prevRop+", "+rop);
					rewrites.set(prevRop.instructionIndex, null); // kill first delete
					rop.index = Math.min(prevRop.index, rop.index);
					rop.lastIndex = Math.max(prevRop.lastIndex, rop.lastIndex);
					System.out.println("new rop "+rop);
				}
				else if ( !disjoint && !same ) {
					throw new IllegalArgumentException("replace op boundaries of "+rop+
													   " overlap with previous "+prevRop);
				}
			}
		}

		// WALK INSERTS
		for (int i = 0; i < rewrites.size(); i++) {
			RewriteOperation op = (RewriteOperation)rewrites.get(i);
			if ( op==null ) continue;
			if ( !(op instanceof InsertBeforeOp) ) continue;
			InsertBeforeOp iop = (InsertBeforeOp)rewrites.get(i);
			// combine current insert with prior if any at same index
			List prevInserts = getKindOfOps(rewrites, InsertBeforeOp.class, i);
			for (int j = 0; j < prevInserts.size(); j++) {
				InsertBeforeOp prevIop = (InsertBeforeOp) prevInserts.get(j);
				if ( prevIop.index == iop.index ) { // combine objects
					// convert to strings...we're in process of toString'ing
					// whole token buffer so no lazy eval issue with any templates
					iop.text = catOpText(iop.text,prevIop.text);
                    // delete redundant prior insert
                    rewrites.set(prevIop.instructionIndex, null);
				}
			}
			// look for replaces where iop.index is in range; error
			List prevReplaces = getKindOfOps(rewrites, ReplaceOp.class, i);
			for (int j = 0; j < prevReplaces.size(); j++) {
				ReplaceOp rop = (ReplaceOp) prevReplaces.get(j);
				if ( iop.index == rop.index ) {
					rop.text = catOpText(iop.text,rop.text);
					rewrites.set(i, null);  // delete current insert
					continue;
				}
				if ( iop.index >= rop.index && iop.index <= rop.lastIndex ) {
					throw new IllegalArgumentException("insert op "+iop+
													   " within boundaries of previous "+rop);
				}
			}
		}
		// System.out.println("rewrites after="+rewrites);
		Map m = new HashMap();
		for (int i = 0; i < rewrites.size(); i++) {
			RewriteOperation op = (RewriteOperation)rewrites.get(i);
			if ( op==null ) continue; // ignore deleted ops
			if ( m.get(new Integer(op.index))!=null ) {
				throw new Error("should only be one op per index");
			}
			m.put(new Integer(op.index), op);
		}
		//System.out.println("index to op: "+m);
		return m;
	}

	protected String catOpText(Object a, Object b) {
		String x = "";
		String y = "";
		if ( a!=null ) x = a.toString();
		if ( b!=null ) y = b.toString();
		return x+y;
	}
	protected List getKindOfOps(List rewrites, Class kind) {
		return getKindOfOps(rewrites, kind, rewrites.size());
	}

    /** Get all operations before an index of a particular kind */
    protected List getKindOfOps(List rewrites, Class kind, int before) {
		List ops = new ArrayList();
		for (int i=0; i<before && i<rewrites.size(); i++) {
			RewriteOperation op = (RewriteOperation)rewrites.get(i);
			if ( op==null ) continue; // ignore deleted
			if ( op.getClass() == kind ) ops.add(op);
		}		
		return ops;
	}

	public String toDebugString() {
		return toDebugString(MIN_TOKEN_INDEX, size()-1);
	}

	public String toDebugString(int start, int end) {
		StringBuffer buf = new StringBuffer();
		for (int i=start; i>=MIN_TOKEN_INDEX && i<=end && i<tokens.size(); i++) {
			buf.append(get(i));
		}
		return buf.toString();
	}
}
