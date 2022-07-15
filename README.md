<h2>Algorítmo do Banqueiro</h2>
Foi desenvolvido uma versão do algorítmo do banqueiro que é um algorítmo para alocação de recursos com prevenção de impasses, essa versão faz uma simulação utilizando processos criados manualmente e como eles se comportam com o tratamento de impasses.</br></br>
O projeto está dividido em cinco packages:

- <b>domain:</b> contém os objetos utilizados na aplicação, relacionados às regras do negócio</br>
  - <i>BankerObj.java</i></br>
Contém as quantidades e vetores dos processos e recursos disponíveis, além das matrizes AllocationA, ClaimC e NeedCA (C-A), as matrizes foram feitas com Listas de <i>ProcessObj</i>, pois possibilita a dinâmica de adicionar e remover processos facilmente.
  - <i>ProcessObj.java</i></br>
Usado como um objeto de um processo efetivamente, contém um Id único e um vetor de recursos, esse vetor é o máximo de recursos que o processo vai precisar para ser executado.
 
- <b>port:</b> contém os portos que se relacionam com a aplicação</br>
  - <i>Cli.java</i></br>
Classe responsável por realizar as impressões para o usuário através do terminal, contém algumas funções úteis para imprimir com formatação específica
  - <i>FileDataInput.java</i></br>
Classe responsável por realizar a leitura dos arquivos de texto que contém as configurações do sistema e parâmetros de cada processo. Dois arquivos .txt serão utilizados, sendo o <i>starterFile.txt</i> e o <i>runtimeFile.txt</i>. O <i>starterFile</i> contém os dados sobre quantos processos e quantos recursos o sistema tem que ter, além da quantidade máxima de recursos disponíveis, e, em seguida, os dados de cada processo para inicializar as matrizes no sistema.
 
- <b>thread:</b> contém as threads utilizadas no programa</br>
  - <i>ThreadNewProcess.java</i></br>
Responsável por inserir novos processos no sistema em tempo de execução, são inseridos 6 processos com um intervalo de 15 segundos entre cada
  - <i>ThreadRunBanker.java</i></br>
Responsável por executar o algoritmo do banqueiro, distribuindo os recursos para cada processo em seu momento de execução e retornando os recursos no sistema. A simulação de execução de um processo é realizada através de um <i>Thread.Sleep</i> de 10 segundos. Isso possibilita a dinâmica de inserir novos processos a cada 15 segundos sem que o sistema termine de executar todos os processos a tempo.;
 
- <b>usecase:</b> contém os casos de uso da aplicação, são classes singleton, ou seja, que realizam apenas uma função</br>
  - <i>AddProcess.java</i></br>
Responsável por adicionar o processo em tempo de execução, é invocada pela <i>ThreadNewProcess</i> a cada 15 segundos. A função carrega todos os parâmetros do processo nas matrizes do programa em execução.
  - <i>CheckSafeState.java</i></br>
Responsável por verificar se o sistema está em estado SAFE/UNSAFE, para isso a função realiza uma execução completa do algoritmo do banqueiro, porém realiza a manipulação em matrizes temporárias, para não afetar o sistema, apenas para validar o estado em que o sistema se encontra.
  - <i>ExecuteBanker.java</i></br>
Executa o algoritmo do banqueiro efetivamente, disponibilizando os recursos para os processos e aguardando a execução dos mesmos, essa execução é simulada com um <i>Thread.Sleep</i> de 10 segundos. A função é invocada pela <i>ThreadRunBanker</i>.
  - <i>LoadProcesses.java</i></br>
Responsável por carregar todos os processos do estado inicial, provenientes do arquivo <i>startFile.txt</i>, ela é executada apenas uma vez no início da execução do programa.
 
- <i>Main.java</i></br>
Classe responsável pela execução do programa, faz a inicialização das classes e objetos necessários, no caso <i>FileDataInput</i>, <i>Cli</i> e o <i>BankerObj</i>, carrega a instância inicial do programa e inicia as Threads: <i>ThreadRunBanker</i> e <i>ThreadNewProcess</i>.
 
<h2 align="center"><b>Fluxogramas</b></h2>

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
 
<h2>Execução</h2>
Duas situações foram criada para simular a execução do programa, os arquivos starterFile.txt e runtimeFile.txt estão nas pastas caso1 e caso2 e para simular cada um deles, basta modificar as linhas 11 e 12 do Main.java para a pasta caso2, ou apenas substituir os arquivos diretamente na pasta caso1.
 
Caso 1, o melhor cenário, novos processos entram, mas continua SAFE:</br>
<i>Entrada:</i>
______________________________________________________________________________
<p align="left">  
  <img width="250" src="https://user-images.githubusercontent.com/38412804/179257708-faf35090-a159-4e35-942e-314993a1e24d.png" alt="Entrada1">
</p>

______________________________________________________________________________
<i>Saída:</i>
______________________________________________________________________________

O starterFile vai gerar as seguintes matrizes no sistema:</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179258294-ca3349bb-9481-4d83-bd57-4e70dde7e5d5.png" alt="Saida1">
</p>

O banker então é iniciado pois está SAFE</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179258547-6db6cfda-9c26-45d1-b97e-5e6ec440f0a9.png" alt="Saida1.1">
</p>
 
Conforme os novos processos são inseridos o banker continua sendo executado, porém vai atualizando a sequência em que cada processo será executado:</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179258701-a9353575-4a60-48d8-8108-65d958dc2910.png" alt="Saida1.2">
</p>

Por fim, a lista de processos executados foi: </br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179258860-9fde3d9d-2d62-4833-8af2-6c8ba3092883.png" alt="Saida1.3">
</p>

______________________________________________________________________________
 
Caso 2, nesse caso o sistema é iniciado como SAFE porém durante a inserção de um processo ele entra em UNSAFE e então não consegue mais prover recursos:</br>
Entrada:
______________________________________________________________________________

<p align="left">  
  <img width="250" src="https://user-images.githubusercontent.com/38412804/179259104-3c4e0eb5-d306-4d09-915d-7093c0f4c8e7.png" alt="Entrada2">
</p>

______________________________________________________________________________
Saída:</br>

______________________________________________________________________________
O starterFile vai gerar as seguintes matrizes no sistema:</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179259286-f478c506-351d-46f4-8e2e-b715c0fed19f.png" alt="Saida1.3">
</p>
 
O banker então é iniciado pois está SAFE</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179259385-30325989-b1e1-4ec1-ab4b-c977f059a9b6.png" alt="Saida1.3">
</p> 
 
Porém após a inserção do P6 o sistema entra em UNSAFE</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179259476-9c6dd865-0281-4ff2-ba92-216c14e02e00.png" alt="Saida1.3">
</p> 
 
 
Por fim, o sistema não sai de UNSAFE mesmo após inserir todos os novos processos:</br>
<p align="left">  
  <img width="500" src="https://user-images.githubusercontent.com/38412804/179259663-f38a00cb-62ca-454e-ac5b-fb18502a3d19.png" alt="Saida1.3">
</p> 

______________________________________________________________________________
 
Conclusão
O algoritmo funciona como esperado, evitando ao máximo os Deadlocks. Porém lidar com a inserção de novos processos pode ser um problema, pois esses processos podem entrar com algum recurso alocado tirando o sistema do estado SAFE, além disso, na simulação não está sendo considerado, mas se um processo demorar muito tempo para ser executado possivelmente isso vai deixar o sistema travado, pois o tempo de execução não é levado em conta.
Outra questão não considerada é de um processo precisar de mais recursos durante a execução fazendo com que o processo trave todo o sistema. Apesar disso, utilizar o algoritmo do banqueiro com algumas modificações  para atender a dinâmica do sistema pode ser bem interessante, principalmente ao lidar com sistemas bem definidos com recursos limitados e processos conhecidos, como um sistema com SO embarcado.
