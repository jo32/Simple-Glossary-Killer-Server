package us.bandj.SimpleGlossaryKiller.Entity;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class User {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key userKey;

	@Persistent
	private String username;

	@Persistent
	private String passworrd;

	@Persistent
	int wordsPerDay;

	public User(String username, String password, int wordsPerDay) {
		super();
		this.userKey = KeyFactory.createKey(User.class.getSimpleName(),
				username);
		this.username = username;
		this.passworrd = password;
		if (wordsPerDay > 0 && wordsPerDay <= 100)
			this.wordsPerDay = wordsPerDay;
		else
			this.wordsPerDay = 20;
	}

	public Key getUserKey() {
		return userKey;
	}

	public void setUserKey(Key userKey) {
		this.userKey = userKey;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassworrd() {
		return passworrd;
	}

	public void setPassworrd(String passworrd) {
		this.passworrd = passworrd;
	}

	public int getWordsPerDay() {
		return wordsPerDay;
	}

	public void setWordsPerDay(int wordsPerDay) {
		this.wordsPerDay = wordsPerDay;
	}

}
