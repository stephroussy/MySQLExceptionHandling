package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import projects.entity.Project;
import projects.exception.DbException;
import provided.util. DaoBase;

//Create class that will read and write to the MySQL database.
@SuppressWarnings("unused")
public class ProjectDao extends DaoBase {
	//add constants with table names
	//a constant is specified using static final
	private static final String CATEGORY_TABLE = "category";
	private static final String MATERIAL_TABLE = "material";
	private static final String PROJECT_TABLE = "project";
	private static final String PROJECT_CATEGORY_TABLE = "project_category";
	private static final String STEP_TABLE = "step";
	
/*To save the project details: 
 * Create the SQL statement 
 * Obtain a Connection and start a transaction. Next you will 
 * Obtain a PreparedStatement and set the parameter values from the Project object
 * Save the data and commit the transaction*/
	
	public Project insertProject(Project project) {
		/*the following SQL statement will insert the values from 
		 * the Project object passed to the insertProject() method*/
		// @formatter:off
		String sql = ""
				+ "INSERT INTO " + PROJECT_TABLE + " " 
				+ " (project_name, estimated_hours, actual_hours, difficulty, notes) "
				+ "VALUES "
				+ "(?, ?, ?, ?, ?)";
		// @formatter: on
		
		try(Connection conn = DbConnection.getConnection()) {
			//start transaction by calling startTransaction() and passing in the Connection object
			startTransaction (conn);
			
			/*add another try-with-resource statement to obtain a PreparedStatement 
			 * object from the Connection object
			 * Pass the SQL statement as a parameter to conn.prepareStatement()*/
			try(PreparedStatement stmt = conn.prepareStatement(sql)) {
				/*Set the project details as parameters in the PreparedStatement object. 
				 * Inside the inner try block, set the parameters on the Statement. 
				 * Use the convenience method in DaoBase setParameter(). 
				 * This method handles null values correctly. 
				 * Add these parameters: projectName, estimatedHours, actualHours, difficulty, and notes. */
				setParameter(stmt, 1, project. getProjectName(), String.class);
				setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class); 
				setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
				setParameter(stmt, 4, project.getDifficulty(), Integer.class);
				setParameter(stmt, 5, project.getNotes(), String.class);
				//perform insert by calling executeUpdate() on the PreparedStatement object
				stmt.executeUpdate();
				
				//obtain project ID (primary key) by calling the convenience method in DaoBase, getLastInsertId()
				//Pass the Connection object & the constant PROJECT_TABLE to getLastInsertId()
				Integer projectId = getLastInsertId(conn, PROJECT_TABLE);
				//Commit the transaction by calling the convenience method in DaoBase, commitTransaction()
				//Pass the Connection object to commitTransaction() as a parameter.
				commitTransaction(conn);
		
				//Set the projectId on the Project object that was passed into insertProject and return it
				project.setProjectId(projectId);
				return project;
				}
			/*add catch block to inner try block
			 * roll back the transaction and throw a DbException initialized 
			 * with the Exception object passed into the catch block
			 * This will ensure that the transaction is rolled back when an exception is thrown*/
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
				}
			}
		catch (SQLException e) {
			throw new DbException(e);
			}
		}
	}