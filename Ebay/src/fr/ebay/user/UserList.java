package fr.ebay.user;

import java.util.HashSet;
import java.util.Iterator;

public class UserList extends HashSet<User>{
	
	//verifier
	private static final long serialVersionUID = 1L;

	public UserList(HashSet<User> users){
		super(users);
	}

	public boolean add(UserList userlist, User user){
		
		Iterator<User> iterator = userlist.iterator();
		while (iterator.hasNext()) {
			User tmp = (User) iterator.next();

			if (tmp.getLogin() == user.getLogin()) {
				System.out
						.println("This login isn't available" + '\n' + "Please choose another login");

				return false;
			}
		}
			
		add(user);
		return true;
		
	}
	}
	
