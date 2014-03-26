package fr.ebay.user;

import java.util.HashSet;
import java.util.Iterator;

import fr.ebay.object.OjectStatus;
import fr.ebay.object.Objet;

public class User {

	protected String login;
	protected String family_name;
	protected String given_name;
	protected HashSet<Role> list_role;
	protected boolean AlertPrixDeReserve;
	protected boolean AlertCancelledObject;
	protected boolean AlertBuyerBid;

	public User(String login, String family_name, String given_name,
			Role role1, Role role2, boolean AlertPrixDeReserve , boolean AlertCancelledObject, boolean AlertBuyerBid) {
		
		this.login = login;
		this.family_name = family_name;
		this.given_name = given_name;
		this.AlertPrixDeReserve = AlertPrixDeReserve;
		this.AlertCancelledObject = AlertCancelledObject;
		this.AlertBuyerBid = AlertBuyerBid;
		
		this.list_role = new HashSet<>();
		// role1
		if (role1 != null)
			list_role.add(role1);

		// role2
		if (role2 != null)
			list_role.add(role2);

	}

	
	//***********CREATION D'OBJET***********
	public HashSet<Objet> createObject(HashSet<Objet> listobjet,
			String summary, int price, int prix_de_reserve, OjectStatus isposted) {

		if(!list_role.contains(Role.seller)){
			System.out.println(given_name + ",You're a Buyer, you can't create object" + '\n' + "your object hasn't been created, sorry");
			return listobjet;
		}
		
		HashSet<Objet> newlist = listobjet;
		Objet newobjet = new Objet(summary, price, prix_de_reserve, isposted,
				this.login);
		newlist.add(newobjet);
		System.out.println(given_name + ", your object has been added to the list");

		return newlist;
		

	}
	
	//***********PLACER UNE OFFRE ***********
	public void createOffer(HashSet<Objet> listobjet, int id, int price){
		
		if(!list_role.contains(Role.buyer)){
			System.out.println(given_name + ",You're a seller, you can't buy object" + '\n' + "your offer was trashed, sorry");
			return;
		}
		
		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
			if(tmp.getId() == id)
			{
				if(tmp.getSellerlogin() == login){
					System.out.println(given_name + ", You can't buy your own object");
					return;
				}
				else{
					if(price > tmp.getPrice())
					{
						if(tmp.getIsposted() == OjectStatus.posted)
						{
							//actualiser listes des alertes de l'objet
							if(AlertBuyerBid)
								if(!tmp.getAlertBuyerBidSet().contains(login))
								{
									HashSet<String> newABB = tmp.getAlertBuyerBidSet();
									newABB.add(login);
									tmp.setAlertBuyerBidSet(newABB);
								}
							
							if(AlertCancelledObject)
								if(!tmp.getAlertCancelledObjectSet().contains(login))
								{
									HashSet<String> newACO = tmp.getAlertCancelledObjectSet();
									newACO.add(login);
									tmp.setAlertCancelledObjectSet(newACO);
								}
							
							if(AlertPrixDeReserve)
								if(!tmp.getAlertPrixDeReserveSet().contains(login))
								{
									HashSet<String> newAPDR = tmp.getAlertPrixDeReserveSet();
									newAPDR.add(login);
									tmp.setAlertPrixDeReserveSet(newAPDR);
								}
							
						tmp.setPrice(price);
						tmp.setBestbidderlogin(login);
						System.out.println("Congrats " + given_name + ", you are the best bidder for this object") ;
						
						for (String ABB : tmp.getAlertBuyerBidSet()) {
							if (ABB != login)
							System.out.println("ALERT: login -> " + ABB + " Someone has overbid");
						}
						
						if(tmp.isPrixDeReserveReached())
						{
							System.out.println("the prix de reserve has been reached");
							for (String APDR : tmp.getAlertPrixDeReserveSet()) {
								if(APDR != login)
								System.out.println("ALERT: login -> " + APDR + " the prix de reserve has been reached");
							}
						}
						else
							System.out.println("the prix de reserve hasn't been reached");
						
						System.out.println("ALERT: login -> " + tmp.getSellerlogin() + " someone is interested by you object");
						return;
						
						
						
						}
						else{
							System.out.println("The object hasn't been published on Ebay, sorry");
							return;
						}
					}
					else
					{
						System.out.println("The actual Amount is " + tmp.getPrice() + ", please enter an higher amount" );
						return;
					}	
				}
			}	
			
		}
		
		System.out.println("none of the objects has id = " + id);
		
		
		
	}
	
	//***********SUPPRIMER UN OBJET ***********
	public void cancelledObject(HashSet<Objet> listobjet, int id){
		
		if(!list_role.contains(Role.seller)){
			System.out.println(given_name + ",You must be a seller to cancel objects");
			return;
		}
		
		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
			if(tmp.getId() == id)
			{
				if(tmp.getSellerlogin() == login)
				{
					if(tmp.isPrixDeReserveReached())
						{
						tmp.setIsposted(OjectStatus.cancelled);
						System.out.println("your object (id = " + id + ") has been cancelled");	
						for (String ACO : tmp.getAlertCancelledObjectSet()) {
							System.out.println("ALERT: login -> " + ACO + " the object : ID -> " + tmp.getId() + " has been cancelled");
						}
						return;
						}
					else
					{
						System.out.println("the object price is higher than the prix de reserve, you can't delete this object");
						return;
					}
				}
				else
				{
					System.out.println("This object doesn't belong you !");
					return;
				}
			}
		}
		
		System.out.println("none of the objects has id = " + id);
		
	}
	
	//***********OBTENIR DES INFORMATION SUR LE PRIX DE RESERVE D'UN OBJET***********
	public void aboutPrixDeReserve(HashSet<Objet> listobjet, int id){
		
		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
			if(tmp.getId() == id)
			{
				if(tmp.getPrixDereserve(login) == -1 ){
					boolean i = tmp.isPrixDeReserveReached();
					if(i){
						System.out.println("the prix de reserve has been reached");
						return;
					}
					System.out.println("the prix de reserve hasn't been reached");
					return;
				}
				else{
					System.out.println("the prix de reserve for this object is " + tmp.getPrixDereserve(login));
				}
					
			
			}
		}
		
		System.out.println("none of the objects has id = " + id);
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFamily_name() {
		return family_name;
	}

	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}

	public String getGiven_name() {
		return given_name;
	}

	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}

}
