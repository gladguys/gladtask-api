package br.com.glad.gladtask;

import br.com.glad.gladtask.entities.Project;
import br.com.glad.gladtask.entities.Task;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8")
	);

	public static List<Task> montaListaTasks() {
		List<Task> tasks = new ArrayList<>();

		Task task1 = new Task();
		String id1 = "5c479d4f1e87b91090e2fa72";
		task1.setId(id1);

		Task task2 = new Task();
		String id2 = "5c47a23c1e87b91090e2fa74";
		task2.setId(id2);

		tasks.add(task1);
		tasks.add(task2);
		return tasks;
	}

	public static List<Project> buildProjectList() {
		List<Project> projects = new ArrayList<>();

		Project project1 = new Project();
		String id1 = "5c479d4f1e87b91090e2fa72";
		project1.setId(id1);

		Project project2 = new Project();
		String id2 = "5c479d4f1e87b91090e2fa72";
		project2.setId(id2);

		projects.add(project1);
		projects.add(project2);
		return projects;
	}
}