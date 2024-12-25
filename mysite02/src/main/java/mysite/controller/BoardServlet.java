package mysite.controller;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.action.board.DeleteAction;
import mysite.controller.action.board.ListAction;
import mysite.controller.action.board.ModifyAction;
import mysite.controller.action.board.ModifyFormAction;
import mysite.controller.action.board.ViewAction;
import mysite.controller.action.board.WriteAction;
import mysite.controller.action.board.WriteFormAction;

@WebServlet("/board")
public class BoardServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;

	private Map<String, Action> mapAction = Map.of(
				"view", new ViewAction(),
				"writeform", new WriteFormAction(),
				"write", new WriteAction(),
				"modifyform", new ModifyFormAction(),
				"modify", new ModifyAction(),
//				"insert", new InsertAction(),
//				"deleteform", new DeleteFormAction(),
				"delete", new DeleteAction()
			);
	
	@Override
	protected Action getAction(String actionName) {
		return mapAction.getOrDefault(actionName, new ListAction());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}