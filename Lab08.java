//Class que contém a variável compartilhada
class Compartilhado{

  public static int compartilhado;

  public synchronized void compartilhado () {
    compartilhado = 0;
  }
}

class LE {
    private int leit, escr, leit_escr;
    
    // Construtor
    LE() { 
       this.leit = 0; //leitores lendo (0 ou mais)
       this.escr = 0; //escritor escrevendo (0 ou 1)
       this.leit_escr = 0; //leitor/ecritor ()
    } 
    
    // Entrada para leitores
    public synchronized void EntraLeitor (int id) {
      try { 
        while (this.escr > 0) {
        //if (this.escr > 0) {
           System.out.println ("le.leitorBloqueado("+id+")");
           wait();  //bloqueia pela condicao logica da aplicacao 
        }
        System.out.println ("le.leitorLendo("+id+")");
        System.out.println("O leito de id " + id + " está executando"); 
        if(Compartilhado.compartilhado % 2  == 0){
          System.out.println("O " + Compartilhado.compartilhado + " eh par");  
        }else{
          System.out.println("O " + Compartilhado.compartilhado + " eh ímpar");  
        }
        this.leit++;  //registra que ha mais um leitor lendo
      } catch (InterruptedException e) { }
    }
    
    // Saida para leitores
    public synchronized void SaiLeitor (int id) {
       this.leit--; //registra que um leitor saiu
       if (this.leit == 0) 
             this.notify(); //libera escritor (caso exista escritor bloqueado)
       System.out.println ("le.leitorSaindo("+id+")");
    }
    
    // Entrada para escritores
    public synchronized void EntraEscritor (int id) {
      try { 
        while ((this.leit > 0) || (this.escr > 0)) {
        //if ((this.leit > 0) || (this.escr > 0)) {
           System.out.println ("le.escritorBloqueado("+id+")");
           wait();  //bloqueia pela condicao logica da aplicacao 
        }
        System.out.println ("le.escritorEscrevendo("+id+")");
        System.out.println("O escritor de id " + id + " está executando");
        System.out.println("O valor da variável compartilhada é " + Compartilhado.compartilhado);
        Compartilhado.compartilhado = id;
        System.out.println("O valor da variável foi alterada para " + Compartilhado.compartilhado);  
        this.escr++; //registra que ha um escritor escrevendo
      } catch (InterruptedException e) { }
    }
    
    // Saida para escritores
    public synchronized void SaiEscritor (int id) {
       this.escr--; //registra que o escritor saiu
       notifyAll(); //libera leitores e escritores (caso existam leitores ou escritores bloqueados)
       System.out.println ("le.escritorSaindo("+id+")");
    }

      // Entrada para escritores e Leitores
      public synchronized void EntraLeitorEscritor (int id) {
        try { 
          while ((this.leit > 0) || (this.escr > 0)) {
          //if ((this.leit > 0) || (this.escr > 0)) {
             System.out.println ("le.leitorescritorBloqueado("+id+")");
             wait();  //bloqueia pela condicao logica da aplicacao 
          }
          System.out.println ("le.leitorescritorLendo&Escrevendo("+id+")");
          System.out.println("O leito&escritor de id " + id + " está executando");
          System.out.println("O valor da variável compartilhada é " + Compartilhado.compartilhado); 
          Compartilhado.compartilhado += 1 ;
          System.out.println("O valor da variável foi alterada para " + Compartilhado.compartilhado); 
          leit_escr++; //registra que ha um leitor lendo e um escritor escrevendo 
        } catch (InterruptedException e) { }
      }
      
      // Saida para escritores e leitores 
      public synchronized void SaiLeitorEscritor (int id) {
         this.leit_escr--; //registra que o leitor e o escritor sairam
         notifyAll(); //libera leitores e escritores (caso existam leitores ou escritores bloqueados)
         System.out.println ("le.leitorescritorSaindo("+id+")");
      }
}
  
  
  
  //Aplicacao de exemplo--------------------------------------------------------
  // Leitor
  class Leitor extends Thread {
    int id; //identificador da thread
    int delay; //atraso bobo
    LE monitor;//objeto monitor para coordenar a lógica de execução das threads
  
    // Construtor
    Leitor (int id, int delayTime , LE m) {
      this.id = id;
      this.delay = delayTime;
      this.monitor = m;
    }
  
    // Método executado pela thread
    public void run () {
      try {
        for (;;) {
          System.out.println("O leitor de id " + this.id + " iniciou"); 
          this.monitor.EntraLeitor(this.id);
          this.monitor.SaiLeitor(this.id);
          sleep(this.delay); 
          
        }
      } catch (InterruptedException e) { return; }
    }
  }
  
  //--------------------------------------------------------
  // Escritor
  class Escritor extends Thread {
    int id; //identificador da thread
    int delay; //atraso bobo...
    LE monitor; //objeto monitor para coordenar a lógica de execução das threads
  
    // Construtor
    Escritor (int id, int delayTime, LE m) {
      this.id = id;
      this.delay = delayTime;
      this.monitor = m;
    }
  
    // Método executado pela thread
    public void run () {
      
      try {
        for (;;) {
          System.out.println("O escritor de id " + this.id + " iniciou"); 
          this.monitor.EntraEscritor(this.id); 
          this.monitor.SaiEscritor(this.id);
          sleep(this.delay);  
        }
      } catch (InterruptedException e) { return; }
    }
  }

  //--------------------------------------------------------
  // Leitor e Escritor
  class LeitorEscritor extends Thread {
    int id; //identificador da thread
    int delay; //atraso bobo...
    LE monitor; //objeto monitor para coordenar a lógica de execução das threads
  
    // Construtor
    LeitorEscritor (int id, int delayTime, LE m) {
      this.id = id;
      this.delay = delayTime;
      this.monitor = m;
    }
  
    // Método executado pela thread
    public void run () {
      
      try {
        for (;;) {
          System.out.println("O leitor&escritor de id " + this.id + " iniciou");  
          double j = 123456789.0, i;
          this.monitor.EntraLeitorEscritor(this.id);
          for (i=0; i<100000000; i++) {j=j/2;} //...loop bobo para simbolizar o tempo de leitura   
          this.monitor.SaiLeitorEscritor(this.id);
          sleep(this.delay);  
        }
      } catch (InterruptedException e) { return; }
    }
  }
  
  //--------------------------------------------------------
  // Classe principal
  class Lab08{
    static final int L = 2; //Número de threads leitora
    static final int E = 4; //Número de threads escritora
    static final int LE = 2; //Número de threads leitora e escritora
  
    public static void main (String[] args) {
      int i;

      LE monitor = new LE();            // Monitor (objeto compartilhado entre leitores e escritores)
      Leitor[] l = new Leitor[L];       // Threads leitores
      Escritor[] e = new Escritor[E];   // Threads escritores
      LeitorEscritor[] le = new LeitorEscritor[LE]; // Threads leitores e escritores

      //inicia o log de saida
      System.out.println ("import verificaLE");
      System.out.println ("le = verificaLE.LE()");
      
      //Inicia as threads leitoras 
      for (i=0; i<L; i++) {
         l[i] = new Leitor(i+1, (i+1)*500, monitor);
         l[i].start(); 
      }
      //Inicia as threads escritoras
      for (i=0; i<E; i++) {
         e[i] = new Escritor(i+1, (i+1)*500, monitor);
         e[i].start(); 
      }
      //Inicia as threads leitoras e escritoras
      for (i=0; i<LE; i++) {
        le[i] = new LeitorEscritor(i+1, (i+1)*500, monitor);
        le[i].start(); 
     }
    }
}
