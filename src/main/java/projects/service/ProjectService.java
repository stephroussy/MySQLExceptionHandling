/*This file acts as a pass-through between the main application file
 *  that runs the menu (ProjectsApp.java) and the DAO file in the data 
 *  layer (ProjectDao.java).*/
package projects.service;

import projects.dao.ProjectDao; 
import projects.entity.Project;


public class ProjectService {
	private ProjectDao projectDao = new ProjectDao();
	
	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	}
}

