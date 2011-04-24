package us.bandj.SimpleGlossaryKiller.Interface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import us.bandj.SimpleGlossaryKiller.Controller.Signal;
import us.bandj.SimpleGlossaryKiller.Model.Manipulator;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class AddWords extends HttpServlet {

	private static final Logger log = Logger.getLogger(Register.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("text/plain");
		PrintWriter pw = null;
		try {
			pw = resp.getWriter();
		} catch (IOException e) {
			log.severe("can not get PrintWritter in Servlet Logins" + e.getMessage());
			e.printStackTrace();
		}
		Gson g = new Gson();
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String words = req.getParameter("words");
		Signal s = Manipulator.addWords(username, password, words);
		pw.print(g.toJson(s));
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("text/plain");
		PrintWriter pw = null;
		try {
			pw = resp.getWriter();
		} catch (IOException e) {
			log.severe("can not get PrintWritter in Servlet Logins" + e.getMessage());
			e.printStackTrace();
		}
		Gson g = new Gson();
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String words = req.getParameter("words");
		Signal s = Manipulator.addWords(username, password, words);
		pw.print(g.toJson(s));
	}
}
