package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDAO {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public void getVertex (Integer nPorzioni, Map<Integer, Food> idMap) {
		String sql = "select distinct f.food_code, f.display_name, count(distinct p.portion_id) as nPorzioni\n" + 
				"from portion p, food f\n" + 
				"where f.food_code = p.food_code\n" + 
				"group by f.food_code, f.display_name\n" + 
				"having nPorzioni <= ?" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, nPorzioni);			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Food f = new Food (res.getInt("f.food_code"), res.getString("f.display_name"));
				idMap.put(f.getFood_code(), f);
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<Adiacenza> getArchi(Map<Integer,Food> idMap){
		String sql = "select f1.food_code, f2.food_code, avg(condiment_calories) as peso\n" + 
				"from food_condiment f1, food_condiment f2, condiment c\n" + 
				"where f1.food_code<f2.food_code\n" + 
				"and f1.food_code<>f2.food_code\n" + 
				"and f1.condiment_code=f2.condiment_code\n" + 
				"and c.condiment_code=f1.condiment_code\n" + 
				"group by f1.food_code, f2.food_code";
		List<Adiacenza> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(idMap.containsKey(res.getInt("f1.food_code")) && idMap.containsKey(res.getInt("f2.food_code"))) {
					Adiacenza a = new Adiacenza (idMap.get(res.getInt("f1.food_code")), 
							idMap.get(res.getInt("f2.food_code")), res.getDouble("peso"));
					result.add(a);
				}
				
			}
			
			conn.close();
			return result;
			
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
