package workset;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import participants.Student;
import participants.Teacher;

public class ChatReader {

	Teacher teach = new Teacher();//teacher object class
	String file = "AOA project\\chatLog.txt"; //name of file
	LinkedList<Student> students = new LinkedList<Student>();//used to store the students names
	
	//gets the name of the teacher
	public String getTeacherName() {
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Please enter the teacher's name: ");
		teach.setName(scan.next());//Stores the name of the teacher
		
		teach.setName("Dr. Arnett Campbell");//remove after
		scan.close();
		return teach.getName();
	}
	
	//Extracts the names of all the students
	public LinkedList<Student> parseChatLog(String teacher) throws FileNotFoundException {
		
		int i = 0;
		int check = 0;
		String timeAsked="", timeAnswered = "";

		
		File chatLog = new File(file);
		Scanner scan = new Scanner(chatLog);
		
		while(scan.hasNextLine()) {
			String line = (scan.nextLine());
			questionCounter(line);//checks to see if a question was asked
			boolean result = line.contains("From");//identifies participants
			
			if(result == true) {
				if(line.contains(teacher)) {
					timeAsked = line.substring(3,5);
					System.out.println("Time asked: " + timeAsked);
					
					continue;//skips this line

				}else {
					//finds the index and extracts the name of the student
					String name = line.substring(13, line.indexOf("To"));
					timeAnswered = line.substring(3,5);
					System.out.println("Time asked: " + timeAnswered);
					
					i=0;//resets the linkedlist index
					
					//check to see if name is already in linkedlist
					for(Student LinkedList: students) {	
						check = 0;//resets the check
						boolean currentStudentName = students.get(i).getName().equals(name);
						
							if(currentStudentName == true) {
								check = 1;

								boolean alreadyAnswered = students.get(i).isAlreadyAnsweredQuestion();
								

								if(alreadyAnswered == false) {

									if(isWithinTime(timeAsked, timeAnswered))
									{

										students.get(i).setAlreadyAnsweredQuestion(true);//changes the status
										students.get(i).setQuestionsAnswered(students.get(i).getQuestionsAnswered()+1);//adds 1 to the student's answered questions
									}

									
								}else{
									students.add(new Student(name, 0, 0.0,true));
									students.get(i).setQuestionsAnswered(students.get(i).getQuestionsAnswered()+1);//adds 1 to the student's answered questions
							}
							break;
						}
						i++;//increments the student linkedlist
						
					}//end of for each
					
					if(check == 0) {
						//adds the student's name to the list	
						students.add(new Student(name, 0, 0.0,true));
						students.get(i).setQuestionsAnswered(students.get(i).getQuestionsAnswered()+1);//adds 1 to the student's answered questions
					}
					
				}	
			}//end of if	
				
			/**/
		}//end of while
		gradeCalculator();//calculates the students grades
		scan.close();
		return students;
		
	}//end of parseChatLog

	//displays the information to the user
	public void display(LinkedList<Student> students) {
		System.out.println(teach.toString()+"\n");//displays the teacher's name
		System.out.println("\n"+students.toString());//displays the names of the students along with the grades
	}
	
	//checks to see if a question was asked
	public void questionCounter(String line) {
		if(line.contains("?")==true) {
			teach.setQuestionsAsked(teach.getQuestionsAsked()+1);
			answeredQuestionsTracker();
		}
	}

	//resets the status of the student answering the question
	public void answeredQuestionsTracker() {
		int i = 0;
		for (Student LinkedList: students) {
			students.get(i).setAlreadyAnsweredQuestion(false);
			i++;
		}
	}

	//calculates the grades of the students
	public void gradeCalculator() {
		int i = 0;

		for(Student LinkedList : students) {
			students.get(i).setGrade(students.get(i).getQuestionsAnswered()/teach.getQuestionsAsked()*100);
			i++;
		}
	}

	public boolean isWithinTime(String timeAsked, String timeAnswered){
 
		int asked = Integer.parseInt(timeAsked);
		int answered = Integer.parseInt(timeAnswered);


		int difference = answered - asked;
		
		//System.out.println(difference);

		return difference < 5; 
		
	}
}
