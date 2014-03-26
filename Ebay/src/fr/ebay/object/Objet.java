package fr.ebay.object;

import java.util.HashSet;
import java.util.Iterator;

public class Objet {

	private static int compteur = 0;
	private String summary;
	// date de fin de l'enchère
	private int price;
	private int prix_de_reserve;
	private int id;
	private OjectStatus objectstatus;
	private String sellerlogin;
	private HashSet<String> AlertPrixDeReserveSet = new HashSet<String>();
	private HashSet<String> AlertCancelledObjectSet = new HashSet<String>();
	private HashSet<String> AlertBuyerBidSet = new HashSet<String>();
	private String bestbidderlogin = null;

	public Objet(String summary, int price, int prix_de_reserve,
			OjectStatus isposted, String sellerlogin) {
		this.setSummary(summary);
		this.setPrice(price);
		this.prix_de_reserve = prix_de_reserve;
		this.setIsposted(isposted);
		this.setSellerlogin(sellerlogin);
		this.id = compteur;
		compteur++;
	}

	public static void getObjectAvailable(HashSet<Objet> listobjet) {
		
		System.out.println("Only posted items are visible" + "\n");
		
		Iterator<Objet> iterator = listobjet.iterator();
		while (iterator.hasNext()) {
			Objet tmp = (Objet) iterator.next();
					if(tmp.getIsposted() == OjectStatus.posted)
					{
					System.out.println("ID " + tmp.getId() + "\n Summary " + tmp.getSummary() + "\n Price " + tmp.getPrice() + '\n');
					}
		}

	}
	
	public boolean isPrixDeReserveReached(){
		if(price >= prix_de_reserve )
			return true;
		return false;
	}
	
	public int getPrixDereserve(String login){
		if(login == sellerlogin)
			return prix_de_reserve;
		return -1;
	}
	
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getId() {
		return id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}



	public OjectStatus getIsposted() {
		return objectstatus;
	}

	public void setIsposted(OjectStatus isposted) {
		this.objectstatus = isposted;
	}

	public String getSellerlogin() {
		return sellerlogin;
	}

	public void setSellerlogin(String sellerlogin) {
		this.sellerlogin = sellerlogin;
	}

	public HashSet<String> getAlertPrixDeReserveSet() {
		return AlertPrixDeReserveSet;
	}

	public void setAlertPrixDeReserveSet(HashSet<String> alertPrixDeReserveSet) {
		AlertPrixDeReserveSet = alertPrixDeReserveSet;
	}

	public HashSet<String> getAlertCancelledObjectSet() {
		return AlertCancelledObjectSet;
	}

	public void setAlertCancelledObjectSet(HashSet<String> alertCancelledObjectSet) {
		AlertCancelledObjectSet = alertCancelledObjectSet;
	}

	public HashSet<String> getAlertBuyerBidSet() {
		return AlertBuyerBidSet;
	}

	public void setAlertBuyerBidSet(HashSet<String> alertBuyerBidSet) {
		AlertBuyerBidSet = alertBuyerBidSet;
	}

	public String getBestbidderlogin() {
		return bestbidderlogin;
	}

	public void setBestbidderlogin(String bestbidderlogin) {
		this.bestbidderlogin = bestbidderlogin;
	}


}
