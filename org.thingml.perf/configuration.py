#
# Copyright (C) 2014 SINTEF <franck.fleurey@sintef.no>
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


#When set to False, does not remove source code after execution. 
#May cause secondary inputs to use files from the first input
deleteTemporaryFiles = True 

#Chooses which compilers should be used
testC = True
testScala = True
testJava = True
perfRetryNumber = 3
perfTransitionNumber = 1000
useYourkit = True

def initPerfConfiguration(graphGenerator):
	conf = graphGenerator.Configuration()
	# conf.setRegions(3,3)
	# conf.setStates(2,4)
	# conf.setOutputs(1,3)
	# conf.setDepth(3)
	# conf.setCompositeRatio(0.5)
	"""~100 states""" 
	conf.setRegions(4,4)
	conf.setStates(3,5)
	conf.setOutputs(1,4)
	conf.setDepth(3)
	conf.setCompositeRatio(0.5)
	# """~500 states""" 
	# conf.setRegions(4,4)
	# conf.setStates(4,6)
	# conf.setOutputs(1,4)
	# conf.setDepth(4)
	# conf.setCompositeRatio(0.5)
	# """~1000 states""" 
	# conf.setRegions(5,8)
	# conf.setStates(5,8)
	# conf.setOutputs(1,4)
	# conf.setDepth(4)
	# conf.setCompositeRatio(0.5)
	
	graphGenerator.launch(conf,2)

#If useBlacklist is True, runs all tests not present in blacklist
#If useBlacklist is False, runs all tests present in whitelist
useBlacklist=True
blacklist=("tester")
whitelist=("testInit")
