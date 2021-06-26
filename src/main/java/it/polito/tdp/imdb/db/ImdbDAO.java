package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void listAllDirectors(Map<Integer, Director> idMapDirector){
		String sql = "SELECT * FROM directors";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				idMapDirector.put(res.getInt("id"), director);
				
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Director> getVertici (Map<Integer, Director> idMapDirector, int anno) {
		String sql = "SELECT DISTINCT md.director_id as id "
				+ "FROM movies m, movies_directors md "
				+ "WHERE m.id = md.movie_id AND m.year = ?";
		List<Director> lista = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = idMapDirector.get(res.getInt("id"));
				lista.add(director);
				
			}
			conn.close();
			return lista;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getArchi (Map<Integer, Director> idMapDirector, int anno) {
		String sql = "SELECT DISTINCT md1.director_id as d1, md2.director_id as d2, COUNT(*) as peso "
				+ "FROM movies m1, movies m2, roles r1, roles r2, movies_directors md1, movies_directors md2 "
				+ "WHERE m1.year = ? AND m2.year = ? "
				+ "AND r1.movie_id = m1.id AND r2.movie_id = m2.id "
				+ "AND md1.movie_id = m1.id AND md2.movie_id = m2.id "
				+ "AND r1.actor_id = r2.actor_id "
				+ "AND md1.director_id < md2.director_id "
				+ "GROUP BY md1.director_id, md2.director_id";
		
		List<Adiacenza> lista = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Adiacenza a = new Adiacenza(idMapDirector.get(res.getInt("d1")), idMapDirector.get(res.getInt("d2")), res.getInt("peso"));
				lista.add(a);
				
			}
			conn.close();
			return lista;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
}
