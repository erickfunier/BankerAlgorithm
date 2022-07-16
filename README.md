<h2>Banker Algorithm</h2>
This is a version of the <a href="https://en.wikipedia.org/wiki/Banker%27s_algorithm">banker algorithm</a> which is an 
algorithm to resource allocation with impass prevention, this version makes a simulation using process created manually 
and how they are managed.</br></br>
The project is separated in five packages:

- <b>domain:</b> related to the business rules</br>
  - <i>BankerObj.java</i></br>
Contains the quantity and arrays of processes and available resources, in addiction to the AllocationA, ClaimC and 
NeedCA (C-A) matrices. The matrices are made with the List of <i>ProcessObj</i> with which we can dinamically add and remove processes.
  - <i>ProcessObj.java</i></br>
Used as a process object, it contains an ID and an array of resources, this array is the maximum of resources necessary 
for the process.
 
- <b>port:</b> the ports related to the application</br>
  - <i>Cli.java</i></br>
Handle the man-machine-interface to the application
  - <i>FileDataInput.java</i></br>
Handle the file data input
 
- <b>thread:</b> contains the threads</br>
  - <i>ThreadNewProcess.java</i></br>
Insert new processes to the application in runtime execution (6 process with 15 sec interval)
  - <i>ThreadRunBanker.java</i></br>
Run the banker algorithm itself. The simulation of a process execution is handled by the <i>Thread.Sleep</i> in 10 sec.
 
- <b>usecase:</b> usecases of the application</br>
  - <i>AddProcess.java</i></br>
Add the process in runtime, invoked by <i>ThreadNewProcess</i> each 15 sec.
  - <i>CheckSafeState.java</i></br>
Check if the system is in SAFE/ UNSAFE state
  - <i>ExecuteBanker.java</i></br>
Run the banker algorithm itself
  - <i>LoadProcesses.java</i></br>
Load the processes to the system from <i>startFile.txt</i>
 
- <i>Main.java</i></br>
Run the application starting all classes used by application
 
<h2 align="center"><b>Flowcharts</b></h2>

<p align="center">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179255620-89c1b1c5-161f-4bbf-a03c-4a2df41292ee.png" alt="Main - ThreadNewProcess">
</p>

<p align="center">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179256307-ab3e9463-d801-4b27-a11e-625c18607056.png" alt="ThreadRunBanker">
</p>

<p align="center">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179256715-75d0d5c5-05a9-437c-8a2e-2607addfbda3.png" alt="CheckSafeState">
</p>

<p align="center">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179257336-71caa943-24b0-461b-b1f7-61a44ce8d03a.png" alt="ExecuteBanker">
</p>
 
<h2>Execution</h2>
Case 1, the best cenario, new process added but keep SAFE:</br>
<i>Input:</i>
______________________________________________________________________________
<p align="left">  
  <img width="250" src="https://user-images.githubusercontent.com/38412804/179257708-faf35090-a159-4e35-942e-314993a1e24d.png" alt="Input1">
</p>

______________________________________________________________________________
<i>Output:</i>
______________________________________________________________________________

The <i>starterFile</i> generate the following matrix:</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179258294-ca3349bb-9481-4d83-bd57-4e70dde7e5d5.png" alt="Output1">
</p>

The banker started in SAFE state</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179258547-6db6cfda-9c26-45d1-b97e-5e6ec440f0a9.png" alt="Output1.1">
</p>
 
As the new processes are add the banker keep running, but updating the sequence of processes</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179258701-a9353575-4a60-48d8-8108-65d958dc2910.png" alt="Output1.2">
</p>

The list of executed process: </br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179258860-9fde3d9d-2d62-4833-8af2-6c8ba3092883.png" alt="Output1.3">
</p>

______________________________________________________________________________
 
Case 2, on this case the system is started in SAFE state but turn to UNSAFE when adding process at runtime:</br>
Input:
______________________________________________________________________________

<p align="left">  
  <img width="250" src="https://user-images.githubusercontent.com/38412804/179259104-3c4e0eb5-d306-4d09-915d-7093c0f4c8e7.png" alt="Input2">
</p>

______________________________________________________________________________
Output:</br>

______________________________________________________________________________
The <i>starterFile</i> generate the following matrix:</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179259286-f478c506-351d-46f4-8e2e-b715c0fed19f.png" alt="Output2.3">
</p>

The banker started in SAFE state</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179259385-30325989-b1e1-4ec1-ab4b-c977f059a9b6.png" alt="Output2.3">
</p> 
 
After insert P6 the system go to UNSAFE state</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179259476-9c6dd865-0281-4ff2-ba92-216c14e02e00.png" alt="Output2.3">
</p> 
 
After that the system can't exit of the UNSAFE:</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179259663-f38a00cb-62ca-454e-ac5b-fb18502a3d19.png" alt="Output2.3">
</p> 

______________________________________________________________________________
 
Conclusion
The algorithm works as expected managing a lot of Deadlocks. But handle new process insertion is a problem, the process 
already enter allocating some resource, and, with that, the system go to UNSAFE state, in addiction to, the simulation 
is not considering the execution time of a process, if a process takes a long time all other process need to wait until 
the end.</br>
Another not considered question is if a process need more resources when it is running, in this case all system will 
stop. Despite that the banker's algorithm is interesting when it is running on a limited and controlled 
operational system, with the knowledge of all processes used by the system.
