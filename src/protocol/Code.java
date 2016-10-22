package protocol;
public final class Code{
         //PROTOCOLLO TIPO DI OPERAZIONE
	    public final static String TYPE_MESSAGE="TYPE_MESSAGE"; //KEY
	    public final static String LOGIN="LOGIN";//VALUE | KEY
	    public final static String LOGOUT ="LOGOUT";//VALUE | KEY
	    public final static String ONLINE_USERS="ONLINE_USERS"; //VALUE
	    public final static String SEND_MESSAGE="SEND_MESSAGE"; //VALUE
	    public final static String MESSAGES_SYNC="MESSAGES_SYNC"; //KEY 
	    
	    //PROTOCOLLO OPERAZIONE EFFETTUATA
	    public final static String STATUS="STATUS"; //KEY
	    public final static String SUCCESS="SUCCESS"; //VALUE
	    public final static String FAILED="FAILED"; //VALUE
	    // {"STATUS":"SUCCESS","TYPE_MESSAGE":"LOGIN"}
	    // {"STATUS":"SUCCESS","TYPE_MESSAGE":"LOGOUT"}

	    
	    //PROTOCOLLO LOGIN UTENTE 
	    public final static String USER_NAME="USER_NAME"; //KEY
	    // {"TYPE_MESSAGE":"LOGIN","USER_NAME":"username"}
	    // {"TYPE_MESSAGE":"LOGOUT","USER_NAME":"Arturo"}


	    
	    //PROTOCOLLO LISTA UTENTI
	    public final static String USERS="USERS"; //KEY
	    // {"TYPE_MESSAGE":"ONLINE_USERS"}
	    // {"TYPE_MESSAGE":"ONLINE_USERS","USERS":["Arturo"]}

	    
	    //PROTOCOLLO INVIO/RICEZIONE MESSAGGI
	    public final static String MESSAGE="MESSAGE"; //KEY
	    public final static String SENDER="SENDER"; //KEY
	    public final static String RECEIVERS="RECEIVERS"; //KEY
	    // {"MESSAGE":"testo","TYPE_MESSAGE":"SEND_MESSAGE","SENDER":"Arturo","RECEIVERS":["Giovanni"]}
	    // [{"MESSAGE":"testo","TYPE_MESSAGE":"SEND_MESSAGE","SENDER":"Arturo","RECEIVERS":["Giovanni"]}]

	    
	    


	    

}
