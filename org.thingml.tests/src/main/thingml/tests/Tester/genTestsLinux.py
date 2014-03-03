#
# Copyright (C) 2011 SINTEF <franck.fleurey@sintef.no>
#
# Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# 	http://www.gnu.org/licenses/lgpl-3.0.txt
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# -*-coding:Latin-1 -*


import sys
import re
import os
from os import listdir
from os.path import isfile, join

os.chdir(r"..")


def parse(fileName):
	file = open(fileName)
	result=''
	for line in file:
		if re.match(r"@conf \".*\"",line):
			confLine = re.sub(r"@conf \"(.*)\"",r"\1",line)
			result=result+'\t'+confLine
			print ("reading"+line)
	file.close()
	return result

mypath = "."
onlyfiles = [ f for f in listdir(mypath) if isfile(join(mypath,f)) ]

for f in onlyfiles:
	match = re.match(r"(.*)\.thingml",f)
	if match is not None:
		name = re.sub(r"(.*)\.thingml",r"\1",f)
		if name != "tester":
			bigname = name[:0]+name[0].upper()+name[1:]
			fichier = open('_linux/'+name+'.thingml', 'w')
			confLines = parse(name+'.thingml')
			fichier.write('import "../../../../../../org.thingml.samples/src/main/thingml/core/_linux/test.thingml"\n'+
			'import "../'+name+'.thingml"\n'+
			'import "../tester.thingml"\n'+
			'import "../../../../../../org.thingml.samples/src/main/thingml/core/_linux/timer.thingml"\n\n'+
			'configuration '+bigname+'C \n@output_folder "/home/thingml_out/" {\n'+
			'	group timer : TimerLinux\n'+
			'		set timer.timer.millisecond = true\n'+
			'		set timer.timer.period = 100\n'+
			'		set timer.clock.period = 100\n\n'+
			'	instance harness : Tester\n'+
			'	instance dump : TestDumpLinux\n'+
			'	instance test : '+bigname+'\n'+
			'	connector test.harness => dump.dump\n'+
			'	connector test.harness => harness.test\n'+
			'	connector harness.testEnd => dump.dump\n'+
			'	connector harness.timer => timer.timer.timer\n'+
			confLines+'}')
