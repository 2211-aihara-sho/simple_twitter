package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.exception.NoRowsUpdatedRuntimeException;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {

	Logger log = Logger.getLogger("twitter");

	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}
//	仕様追加②で追記するコード
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName()+
				" : "+ new Object(){}.getClass().getEnclosingMethod().getName());

		String messageId = request.getParameter("message_id");
		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();

		Message message = null;

		if (!StringUtils.isBlank(messageId) && messageId.matches("^[0-9]*$")) {
			int id = Integer.parseInt(messageId);
			message = new MessageService().select(id);
		}

		if(message != null) {
			request.setAttribute("message", message);
			request.getRequestDispatcher("edit.jsp").forward(request, response);
		} else {
			errorMessages.add("不正なパラメータが入力されました");
			session.setAttribute("errorMessages",  errorMessages);
			response.sendRedirect("./");
		}
	}
//	仕様追加②で追記するコード
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName()+
				" : "+ new Object(){}.getClass().getEnclosingMethod().getName());

		List<String> errorMessages = new ArrayList<String>();

		Message message = getMessage(request);
		if (isValid(message, errorMessages)) {
			try {
				new MessageService().update(message);

			} catch (NoRowsUpdatedRuntimeException e) {
				log.warning("他の人によって更新されています。最新のデータを表示しました。データを確認してください。");
				errorMessages.add("他の人によって更新されています。最新のデータを表示しました。データを確認してください。");
			}
		}

		if (errorMessages.size() != 0) {
			request.setAttribute("errorMessages", errorMessages);
			request.setAttribute("message", message);
			request.getRequestDispatcher("edit.jsp").forward(request, response);
			return;
		}
		response.sendRedirect("./");
	}

//	仕様追加②で追記するコード
	private Message getMessage(HttpServletRequest request) throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName()+
				" : "+ new Object(){}.getClass().getEnclosingMethod().getName());

		Message message = new Message();
		message.setId(Integer.parseInt(request.getParameter("id")));
		message.setText(request.getParameter("text"));
		return message;
	}

	private boolean isValid(Message message, List<String> errorMessages) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName()+
				" : "+ new Object(){}.getClass().getEnclosingMethod().getName());

		if (StringUtils.isBlank(message.getText())) {
			errorMessages.add("メッセージを入力してください");
		} else if (140 < message.getText().length()) {
			errorMessages.add("140文字以下で入力してください");
		}

		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}
}
