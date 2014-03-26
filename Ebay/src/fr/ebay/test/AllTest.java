package fr.ebay.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.ebay.object.Objet;
import fr.ebay.object.OjectStatus;
import fr.ebay.user.Role;
import fr.ebay.user.User;
import fr.ebay.user.UserList;

public class AllTest {

	private static HashSet<Objet> listobjet;
	private static HashSet<User> users ;
	private static UserList userlist;
	
	static User Antoine;
	static User Pierre;
	static User Paul;
	static User alertBuyer;
	
	@BeforeClass
	public static void testSetup() {
		
		 listobjet = new HashSet<Objet>();
		 users = new HashSet<User>();
		 userlist = new UserList(users);
		//User user1 = new User( login, family_name, given_name , role1 , role2 , Alerte_prix_de_reserve , Alerte_Cancelled_object , Alerte_Buyer_bid 
				 
		Antoine = new User("AD", "DEVIN", "Antoine", Role.seller, null ,false,false,false);
		Pierre = new User("PL", "Langrenay", "Pierre", Role.buyer, null ,false,false,false);	
		Paul = new User("PM", "Marret", "Paul", Role.buyer, Role.seller,false,false,false);
		alertBuyer = new User("AB","ALERT","BUYER",Role.buyer,null,true,true,true);
	  
		userlist.add(userlist,Antoine);
		userlist.add(userlist,Pierre);
		userlist.add(userlist,Paul);
		
		System.out.println("\n*****DEBUT BEFORECLASS*****\n");
		
		listobjet = Paul.createObject(listobjet, "gameboy advance", 15, 30,
				OjectStatus.posted);		// objet -> id = 0
		listobjet = Antoine.createObject(listobjet, "PS3", 150, 300,
				OjectStatus.posted);		// objet -> id = 1
		listobjet = Paul.createObject(listobjet, "gameboy color", 15, 30,
				OjectStatus.posted);	    // objet -> id = 2   modifier dans un test 
		listobjet = Antoine.createObject(listobjet, "PS5", 1500, 3000,
				OjectStatus.created);		// objet -> id = 3		
		listobjet = Antoine.createObject(listobjet, "error", 1500, 30000,
				OjectStatus.posted);		// objet -> id = 4	 
		listobjet = Antoine.createObject(listobjet, "error2", 1500, 30000,
				OjectStatus.posted);		// objet -> id = 5	 cancelled dans un test 
		listobjet = Antoine.createObject(listobjet, "alerttest", 15, 30,
				OjectStatus.posted);		// objet -> id = 6	 
		alertBuyer.createOffer(listobjet, 6, 150);
		Objet.getObjectAvailable(listobjet);
		
		System.out.println("*****FIN BEFORECLASS*****\n\n");
	}

	@Test 
	public void testBuyerCreate()	//pas de motif
	{
		int init = listobjet.size();
		
		listobjet = Pierre.createObject(listobjet, "gameboy color", 15, 30,
				OjectStatus.created);
		
		int finale = listobjet.size();
		
		assertEquals("testBuyerCreate", init ,finale);
		//aucun objet n'a ete creer car l'utilisateur est un buyer	
		
		
	}
	
	@Test
	public void offerSeller()	//pas de motif
	{

		Antoine.createOffer(listobjet, 0, 40);

		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
			if(tmp.getId() == 0)
			{
				assertEquals("pricechanged", 15,tmp.getPrice());
				// le prix initial de l'objet n'a pas change 
				assertNull("bestbidder", tmp.getBestbidderlogin());
				// personne n'est le meilleur encherisseur
			}
		}
		
	}
	
	
	@Test
	public void offerOwnObject()	//pas de motif
	{

		Paul.createOffer(listobjet, 0, 40);
		
		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
			if(tmp.getId() == 0)
			{
		assertEquals("pricechanged", 15,tmp.getPrice());
		// le prix initial de l'objet n'a pas change 
		assertNull("bestbidder", tmp.getBestbidderlogin());
		// personne n'est le meilleur encherisseur
			}
		}
		
	}
	
	
	
	@Test
	public void offerlowprice()		//pas de motif
	{
		
		Pierre.createOffer(listobjet, 0, 10);
		
		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
			if(tmp.getId() == 0)
			{
		assertEquals("pricechanged", 15,tmp.getPrice());
		// le prix initial de l'objet n'a pas change 
		assertNull("bestbidder", tmp.getBestbidderlogin());
		// personne n'est le meilleur encherisseur
			}
		}
		
	}
	
	
	
	
	@Test
	public void offerOK()		//prix 40 id 2	
	{

		Pierre.createOffer(listobjet, 2, 40);

		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
			if(tmp.getId() == 2)
			{
		assertEquals("pricechanged", 40,tmp.getPrice());
		// le prix initial de l'objet a change 
		assertEquals("Bestbidder", "PL",tmp.getBestbidderlogin());
		// personne n'est le meilleur encherisseur
			}
		}
	}
	
	@Test
	public void offerObjectonlycreated()		//pas de motif
	{
		
		Pierre.createOffer(listobjet, 3, 10000);
		
		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
			if(tmp.getId() == 3)
			{
		assertEquals("pricechanged", 1500,tmp.getPrice());
		// le prix initial de l'objet n'a pas change 
		assertNull("bestbidder", tmp.getBestbidderlogin());
		// personne n'est le meilleur encherisseur
			}
		}
		
	}
	
	@Test
	public void objetcancelledPDR()	//pas de motif
	{
		
		Antoine.cancelledObject(listobjet, 4);

		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
			if(tmp.getId() == 4)
			{
				assertEquals("pricechanged", OjectStatus.posted ,tmp.getIsposted());
				// le prix de reserve n'a pas ete depasser
			}
		}
		
	}
	
	@Test
	public void objetcancelledOK()	//pas de motif
	{
		Pierre.createOffer(listobjet, 5, 150000);
		Antoine.cancelledObject(listobjet, 5);
		
		

		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
			if(tmp.getId() == 5)
			{
				assertEquals("pricechanged", OjectStatus.cancelled ,tmp.getIsposted());
				// le prix de reserve n'a pas ete depasser
			}
		}
		
	}
	
	@Test
	public void alertobjet1()	//pas de motif
	{
		
		
		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
			if(tmp.getId() == 6)
			{
				assertTrue("alert1",tmp.getAlertBuyerBidSet().contains("AB"));
				//premier type d'alerte
			}
		}
		
	}
	
	@Test
	public void alertobjet2()	//pas de motif
	{
		
		
		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
			if(tmp.getId() == 6)
			{
				assertTrue("alert1",tmp.getAlertCancelledObjectSet().contains("AB"));
				//premier type d'alerte
			}
		}
		
	}
	
	@Test
	public void alertobjet3()	//pas de motif
	{
		
		
		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
			if(tmp.getId() == 6)
			{
				assertTrue("alert1",tmp.getAlertPrixDeReserveSet().contains("AB"));
				//premier type d'alerte
			}
		}
		
	}
	
}
