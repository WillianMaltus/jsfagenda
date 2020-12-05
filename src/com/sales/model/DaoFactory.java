package com.sales.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.faces.context.FacesContext;

import com.sales.control.ContatoBean;

//Classe utilizada para manutenção dos dados no Banco de dados (CRUD)
//Mysql 5.17

public class DaoFactory {

	// Classes para manipulação dos dados
	public static Statement stm; // fornece um ambiente para a sentença SQL
	public static Connection con; // Gerencia a conexão com BD
	public static ResultSet rs; // Mantém a lista dos dados lidos no BD
	public static PreparedStatement pstm; // prepara o ambiente para SQL

	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String db_url = "jdbc:mysql://jg4kf32tgxhrjit7:nxjriofpqk0n1kc7@u3y93bv513l7zv6o.chr7pe7iynqr.eu-west-1.rds.amazonaws.com:3306/e0wu224nckvwb21r?useTimezone=true&serverTimezone=UTC";
			String db_usuario = "root";
			String db_senha = "postgres";
			con = DriverManager.getConnection(db_url, db_usuario, db_senha);
			System.out.println("Conectado");
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} catch (ClassNotFoundException cex) {
			cex.printStackTrace();
		}
		return con;
	}

	public static ArrayList<ContatoBean> getListaContatos() {
		ArrayList<ContatoBean> contatosList = new ArrayList<ContatoBean>();
		try {
			stm = getConnection().createStatement();
			rs = stm.executeQuery("SELECT * FROM contatos");
			while (rs.next()) {
				// objeto para transição de dados
				ContatoBean contatoBean = new ContatoBean();
				contatoBean.setId(rs.getInt("contato_id"));
				contatoBean.setNome(rs.getString("contato_nome"));
				contatoBean.setEmail(rs.getString("contato_email"));
				contatoBean.setSenha(rs.getString("contato_senha"));
				contatoBean.setSexo(rs.getString("contato_sexo"));
				contatoBean.setEndereco(rs.getString("contato_endereco"));
				// compondo a lista que será apresentada na página JSF
				contatosList.add(contatoBean);
			}
			con.close();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return contatosList;
	}

	// método para edição das informações dos contatos
	public static String buscarContato(int id) {
		ContatoBean contatoBean = null;
		//configurar objeto do contato na sessão
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			stm = getConnection().createStatement();
			rs = stm.executeQuery("select * from contatos where contato_id = " + id);
			if (rs != null) {
				rs.next();
				contatoBean = new ContatoBean();
				contatoBean.setId(rs.getInt("contato_id"));
				contatoBean.setNome(rs.getString("contato_nome"));
				contatoBean.setEmail(rs.getString("contato_email"));
				contatoBean.setSexo(rs.getString("contato_sexo"));
				contatoBean.setEndereco(rs.getString("contato_endereco"));
				contatoBean.setSenha(rs.getString("contato_senha"));
			}
			sessionMap.put("contatoSessao", contatoBean);
			con.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return "/editarContato.xhtml?faces-redirect=true";
	}

	public static String atualizarContato(ContatoBean c) {
		System.out.println("Nome: "+c.getNome());
		try {
			pstm = getConnection().prepareStatement("update contatos set contato_nome =?,"
					+ "contato_email =?, contato_senha =?, contato_sexo =?,"
					+ "contato_endereco =? where contato_id=?");
			pstm.setString(1, c.getNome());
			pstm.setString(2, c.getEmail());
			pstm.setString(3, c.getSenha());
			pstm.setString(4, c.getSexo());
			pstm.setString(5, c.getEndereco());
			pstm.setInt(6, c.getId());
			pstm.executeUpdate();
			con.close();
					
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return "/listarcontatos.xhtml?faces-redirect=true";
	}
}
