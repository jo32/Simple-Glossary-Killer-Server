package us.bandj.SimpleGlossaryKiller.Model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import us.bandj.SimpleGlossaryKiller.Controller.Signal;
import us.bandj.SimpleGlossaryKiller.Controller.WordView;
import us.bandj.SimpleGlossaryKiller.Entity.User;
import us.bandj.SimpleGlossaryKiller.Entity.Word;

public class Manipulator {

	private static PersistenceManager pm = PMF.get().getPersistenceManager();

	public static Signal register(String username, String password) {

		Signal signal = null;

		if (username == null || password == null) {
			signal = new Signal(0, "Parameter 'username' or 'password' Not Found!");
		} else if (!validateUsername(username) || !validatePassword(password)) {
			signal = new Signal(0, "Parameter 'username' or 'password' is not valid!");
		} else {
			Query q = pm.newQuery(User.class);
			q.setFilter("username == usernameParam");
			q.declareParameters("String usernameParam");
			@SuppressWarnings("unchecked")
			List<User> results = (List<User>) q.execute(username);
			q.closeAll();
			if (results.size() > 0) {
				signal = new Signal(0, "the user '" + username + "' already exists!");
			} else {
				User user = new User(username, password, 20);
				pm.makePersistent(user);
				signal = new Signal(1, "the user '" + username + "' created!");
			}
		}
		return signal;
	}

	public static Signal changeWords(String username, String password, String words) {

		Signal signal = null;

		Gson g = new Gson();

		if (username == null || password == null || words == null) {
			signal = new Signal(0, "Parameter 'username' or 'password' Not Found!");
		} else if (!validateUsername(username) || !validatePassword(password)) {
			signal = new Signal(0, "Parameter 'username' or 'password' is not valid!");
		} else {
			Key userKey = KeyFactory.createKey(User.class.getSimpleName(), username);
			User user = null;
			try {
				user = (User) pm.getObjectById(User.class, userKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (user == null) {
				signal = new Signal(0, "can not retrieve the user object!");
			} else {
				Type listType = new TypeToken<List<WordView>>() {
				}.getType();
				List<WordView> _words = null;
				try {
					_words = g.fromJson(words, listType);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (_words == null) {
					signal = new Signal(0, "can not recognize the words format!");
				} else {
					boolean result = true;
					for (int i = 0; i < _words.size(); i++) {
						try {
							Key k = KeyFactory.createKey(userKey, Word.class.getSimpleName(), _words.get(i).getWord());
							// Key k2 = _words.get(i).getWordKey();
							Word _word = pm.getObjectById(Word.class, k);
							_word.setFamiliarity(_words.get(i).getFamiliarity());
							pm.makePersistent(_word);
						} catch (Exception e) {
							e.printStackTrace();
							result = false;
						}
						if (result == false)
							break;
					}
					pm.close();
					pm = PMF.get().getPersistenceManager();
					if (result == false) {
						signal = new Signal(0, "failed when making persitent!");
					} else {
						signal = new Signal(1, "ok");
					}
				}
			}
		}

		return signal;
	}

	@SuppressWarnings("unchecked")
	public static Signal getWords(String username, String password) {
		Signal signal = null;

		if (username == null || password == null) {
			signal = new Signal(0, "Parameter 'username' or 'password' Not Found!");
		} else if (!validateUsername(username) || !validatePassword(password)) {
			signal = new Signal(0, "Parameter 'username' or 'password' is not valid!");
		} else {
			Key userKey = KeyFactory.createKey(User.class.getSimpleName(), username);
			User user = null;
			try {
				user = (User) pm.getObjectById(User.class, userKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (user == null) {
				signal = new Signal(0, "can not retrieve the user object!");
			} else {
				if (!user.getUsername().equals(username) || !user.getPassworrd().equals(password)) {
					signal = new Signal(0, "Parameter 'username' or 'password' is not correct!");
				} else {
					Query q = pm.newQuery(Word.class);
					q.setFilter("userKey == userKeyParam && familiarity == familiarityParam");
					q.declareParameters(Key.class.getName() + " userKeyParam, int familiarityParam");
					List<Word> words = new ArrayList<Word>();
					q.setRange(0, user.getWordsPerDay());
					List<Word> words0 = (List<Word>) q.execute(user.getUserKey(), 0);
					q.setRange(0, user.getWordsPerDay());
					List<Word> words1 = (List<Word>) q.execute(user.getUserKey(), 1);
					q.setRange(0, user.getWordsPerDay());
					List<Word> words2 = (List<Word>) q.execute(user.getUserKey(), 2);
					q.setRange(0, user.getWordsPerDay());
					List<Word> words3 = (List<Word>) q.execute(user.getUserKey(), 3);
					q.setRange(0, user.getWordsPerDay());
					List<Word> words4 = (List<Word>) q.execute(user.getUserKey(), 4);
					q.closeAll();
					words.addAll(words0);
					words.addAll(words1);
					words.addAll(words2);
					words.addAll(words3);
					words.addAll(words4);
					List<WordView> wvs = new ArrayList<WordView>();
					for (int i = 0; i < words.size(); i++) {
						wvs.add(new WordView(words.get(i).getWord(), words.get(i).getFamiliarity()));
					}
					signal = new Signal(1, wvs);
				}
			}
		}

		return signal;
	}

	public static Signal addWords(String username, String password, String words) {
		Signal signal = null;

		if (username == null || password == null || words == null) {
			signal = new Signal(0, "Parameter 'username' or 'password' Not Found!");
		} else if (!validateUsername(username) || !validatePassword(password)) {
			signal = new Signal(0, "Parameter 'username' or 'password' is not valid!");
		} else {
			Key userKey = KeyFactory.createKey(User.class.getSimpleName(), username);
			User user = null;
			try {
				user = (User) pm.getObjectById(User.class, userKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (user == null) {
				signal = new Signal(0, "can not retrieve the user object!");
			} else {
				if (!user.getUsername().equals(username) || !user.getPassworrd().equals(password)) {
					signal = new Signal(0, "Parameter 'username' or 'password' is not correct!");
				} else {
					String[] _words = words.split("\\u007C");
					int wordsAdded = 0;
					for (int i = 0; i < _words.length; i++) {
						if (validateWordsFormat(_words[i])) {
							Key wordKey = KeyFactory.createKey(userKey, Word.class.getSimpleName(), _words[i]);
							Word _word = null;
							try {
								_word = (Word) pm.getObjectById(Word.class, wordKey);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (_word == null) {
								_word = new Word(_words[i], user.getUserKey());
								pm.makePersistent(_word);
								wordsAdded++;
							}
						}
					}
					signal = new Signal(1, wordsAdded);
				}
			}
		}
		return signal;
	}

	public static Signal login(String username, String password) {

		Signal signal = null;

		if (username == null || password == null) {
			signal = new Signal(0, "Parameter 'username' or 'password' Not Found!");
		} else if (!validateUsername(username) || !validatePassword(password)) {
			signal = new Signal(0, "Parameter 'username' or 'password' is not valid!");
		} else {
			Query q = pm.newQuery(User.class);
			q.setFilter("username == usernameParam");
			q.declareParameters("String usernameParam");
			@SuppressWarnings("unchecked")
			List<User> results = (List<User>) q.execute(username);
			q.closeAll();
			if (results.size() > 0) {
				String _username = results.get(0).getUsername();
				String _password = results.get(0).getPassworrd();
				if (username.equals(_username) && password.equals(_password)) {
					signal = new Signal(1, "Parameter 'username' or 'password' is right!");
				} else {
					signal = new Signal(0, "Parameter 'username' or 'password' is not coorect!");
				}
			} else {
				signal = new Signal(0, "This user does not exist!");
			}
		}

		return signal;
	}

	public static boolean validateUsername(String username) {
		if (username.matches("[\\w]{4,20}"))
			return true;
		else
			return false;
	}

	public static boolean validatePassword(String password) {
		if (password.matches("[\\w]{4,20}"))
			return true;
		else
			return false;
	}

	public static boolean validateWordsFormat(String word) {
		if (word.matches("[a-zA-Z]+"))
			return true;
		else
			return false;
	}
}
