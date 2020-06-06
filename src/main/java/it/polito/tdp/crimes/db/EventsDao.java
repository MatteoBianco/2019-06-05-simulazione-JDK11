package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.crimes.model.DistrictLatLon;
import it.polito.tdp.crimes.model.Event;



public class EventsDao {
	
	public List<Event> listEventsByDay(LocalDate date) {
		String sql = "SELECT * FROM events WHERE DATE(reported_date) = ? "
				+ "ORDER BY reported_date" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			st.setDate(1, Date.valueOf(date));
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public Integer getStartDistrictId(Integer year) {
		String sql = "SELECT district_id, COUNT(*) AS crimes " + 
				"FROM EVENTS " + 
				"WHERE YEAR(reported_date) = ? " + 
				"GROUP BY district_id" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			Integer district = 0;
			
			Integer minCrimes = -1;
			
			PreparedStatement st = conn.prepareStatement(sql) ;
						
			st.setInt(1, year);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(res.getInt("crimes") < minCrimes || minCrimes == -1) {
					minCrimes = res.getInt("Crimes");
					district = res.getInt("district_id");
				}
			}
			
			conn.close();
			return district;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getDistrictsId() {
		String sql = "SELECT DISTINCT district_id FROM EVENTS ORDER BY district_id" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> result = new ArrayList<>();
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getInt("district_id"));
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<DistrictLatLon> getDistrictsLatLon(Integer year) {
		String sql = "SELECT e.district_id, AVG(e.geo_lat) AS lat, AVG(e.geo_lon) AS lon " + 
				"FROM EVENTS AS e " + 
				"WHERE YEAR(e.reported_date) = ? " + 
				"GROUP BY e.district_id" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<DistrictLatLon> result = new ArrayList<>();
			
			st.setInt(1, year);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(new DistrictLatLon(res.getInt("e.district_id"), new LatLng(res.getDouble("lat"), res.getDouble("lon"))));
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Integer> getYearsCrimes() {
		String sql = "SELECT DISTINCT YEAR(reported_date) AS `year` FROM EVENTS ORDER BY `year`" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> result = new ArrayList<>();
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getInt("year"));
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Integer> getMonthCrimes(Integer year) {
		String sql = "SELECT DISTINCT MONTH(reported_date) AS `month` FROM EVENTS WHERE YEAR(reported_date) = ? ORDER BY `month`" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> result = new ArrayList<>();
			
			st.setInt(1, year);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getInt("month"));
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}	
	}

	public List<Integer> getDayCrimes(Integer year, Integer month) {
		String sql = "SELECT DISTINCT DAY(reported_date) AS `day` FROM EVENTS WHERE YEAR(reported_date) = ? AND MONTH (reported_date) = ? ORDER BY `day`" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> result = new ArrayList<>();
			
			st.setInt(1, year);
			
			st.setInt(2, month);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getInt("day"));
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}	
	}

	public Integer crimesInDay(LocalDate date) {
		String sql = "SELECT COUNT(*) AS num FROM events "
				+ "WHERE DATE(reported_date) = ?" ;
		try {
			Connection conn = DBConnect.getConnection() ;
			
			PreparedStatement st = conn.prepareStatement(sql) ;
						
			st.setDate(1, Date.valueOf(date));
			
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				conn.close();
				return res.getInt("num");
			}
			conn.close();
			return null;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

}
