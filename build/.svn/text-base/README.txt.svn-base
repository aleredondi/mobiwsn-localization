

Il middleware MobiWSN e' realizzato in Java ed i componenti principali sono 4:

- WSNGatewayManager;
- WSNGateway;
- console_client;
- localization_client.

---------------------------------

Per far partire la dimostrazione il comando è il seguente:

Linux:
java -cp <directory>/VIGLIO/demo.jar 
     -Djava.rmi.server.codebase=file:<directory>/VIGLIO/demo.jar
     -Djava.rmi.server.hostname=127.0.0.1
     common.ProjectMain serial@<COM_PORT>:telosb

Windows:
java -cp file:/C:\\<path>\\VIGLIO\\demo.jar 
     -Djava.rmi.server.codebase=file:/C:\\<path>\\VIGLIO\\demo.jar
     -Djava.rmi.server.hostname=127.0.0.1
     common.ProjectMain serial@<COM_PORT>:telosb

  dove :

  - la codebase indica la cartella con i file .class del progetto (e quindi il file jar)
  - hostname specifica di usare un certo indirizzo ip usato da RMI (questo è stato posto al localhost);
  - "common.ProjectMain serial@<COM_PORT>:telosb" indica la classe principale da eseguire e la COM necessaria per ricevere
    ed inviare i messaggi sulla seriale

Class-Path: lib/tinyos.jar lib/AbsoluteLayout.jar lib/appframework-1.0.3.jar lib/jcommon-1.0.16.jar lib/jfreechart-1.0.13.jar lib/jgraph.jar lib/jsr-275-1.0-beta-2.jar lib/swing-layout-1.0.3.jar lib/swing-worker-1.1.jar lib/localization lib/Jama.jar
