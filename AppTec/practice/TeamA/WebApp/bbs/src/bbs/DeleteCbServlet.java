package bbs;

import static bbs.util.CloseableUtil.*;
import static bbs.util.DBUtil.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/deleteCb"})
public class DeleteCbServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");

		int cbId = Integer.parseInt(request.getParameter("cbId"));

		Connection connection = null;
    	PreparedStatement psCb = null;
    	PreparedStatement psCm = null;

		try {
			connection = getConnection();

	      	//投稿を削除するSQL文
	      	StringBuilder sqlCb = new StringBuilder();
	      	sqlCb.append("DELETE FROM contributions ");
	      	sqlCb.append("WHERE id=");
	      	sqlCb.append(cbId);


	      	//コメントを削除するSQL文
	      	StringBuilder sqlCm = new StringBuilder();
	      	sqlCm.append("DELETE FROM comments ");
	      	sqlCm.append("WHERE contribution_id=");
	      	sqlCm.append(cbId);

	      	System.out.println(sqlCm.toString());

	      	//SQLの実行
	      	psCm = connection.prepareStatement(sqlCm.toString());
	      	psCm.executeUpdate();
	      	psCb = connection.prepareStatement(sqlCb.toString());
	      	psCb.executeUpdate();

	      	commit(connection);
	      	request.getRequestDispatcher("/contributionIndex").forward(request, response);

			close(connection);

	      	} catch (RuntimeException | SQLException e) {
	    		rollback(connection);
	    	} catch (Error e) {
	    		rollback(connection);
	    		throw e;
	    	} finally {
	    		close(psCb);
	    		close(psCm);
	    	}
	}
}
