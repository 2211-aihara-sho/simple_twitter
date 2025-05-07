package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/deleteMessage" })
public class DeleteMessageServlet extends HttpServlet {
	Logger log = Logger.getLogger("twitter");

//	仕様追加①つぶやき削除で追記するコード
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName()+
			" : "+ new Object(){}.getClass().getEnclosingMethod().getName());

		String messageId = request.getParameter("message_id");
		int id = Integer.parseInt(messageId);

		new MessageService().delete(id);

		response.sendRedirect("./");
	}
}
