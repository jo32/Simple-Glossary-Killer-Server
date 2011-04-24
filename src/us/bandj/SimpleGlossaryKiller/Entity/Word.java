package us.bandj.SimpleGlossaryKiller.Entity;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class Word {

	@SuppressWarnings("unused")
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key wordKey;

	@Persistent
	private String word;

	@Persistent
	private Key userKey;

	@Persistent
	private int familiarity;

	public Word(String word, Key userKey) {
		this.word = word;
		this.wordKey = KeyFactory.createKey(userKey, Word.class.getSimpleName(), word);
		this.userKey = userKey;
		this.familiarity = 0;
	}

	public Key getWordKey() {
		return this.userKey;
	}

	public Key getWordKey(Key userKey) {
		return KeyFactory.createKey(userKey, Word.class.getSimpleName(), word);
	}

	public void setWordKey(Key wordKey) {
		this.wordKey = wordKey;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Key getUserKey() {
		return userKey;
	}

	public void setUserKey(Key userKey) {
		this.userKey = userKey;
	}

	public int getFamiliarity() {
		return familiarity;
	}

	public void setFamiliarity(int familiarity) {
		this.familiarity = familiarity;
	}

}
