package org.example.serverapp.util;

import java.util.Random;

public class NameUtils {
	private final static String[] names = {"James", "John", "William", "Michael", "David", "Richard", "Joseph", "Charles",
		"Christopher", "Daniel", "Matthew", "Anthony", "Donald", "Mark", "Paul", "Steven", "Andrew", "Kenneth", "Joshua",
		"George", "Kevin", "Brian", "Edward", "Jason", "Timothy", "Jeffrey", "Ryan", "Jacob", "Nicholas", "Eric",
		"Jonathan", "Brandon", "Adam", "Jeremy", "Gary", "Scott", "Justin", "Benjamin", "Samuel", "Patrick", "Travis",
		"Gregory", "Alexander", "Tyler", "Jesse", "Ethan", "Keith", "Brett", "Phillip", "Kyle", "Terry", "Frank", "Stephen",
		"Aaron", "Harold", "Roger", "Lawrence", "Bryan", "Billy", "Albert", "Roy", "Wayne", "Eugene", "Randy", "Howard",
		"Billy", "Vincent", "Louis", "Bobby", "Johnny", "Chris", "Ray", "Gerald", "Wayne", "Lloyd", "Adam", "Billy",
		"Harry", "Eugene", "Shawn", "Leonard", "Nathan", "Norman", "Earl", "Allen", "Evan", "Albert", "Terry", "Franklin",
		"Rodney", "Lee", "Melvin", "Leroy", "Christian", "Kent", "Joel", "Calvin", "Clifford", "Ray", "Joel", "Mary",
		"Patricia", "Jennifer", "Elizabeth", "Linda", "Barbara", "Susan", "Jessica", "Margaret", "Sarah", "Karen", "Nancy",
		"Lisa", "Betty", "Dorothy", "Sandra", "Ashley", "Kimberly", "Donna", "Carol", "Michelle", "Amanda", "Emily",
		"Melissa", "Deborah", "Stephanie", "Laura", "Rebecca", "Sharon", "Cynthia", "Kathleen", "Amy", "Anna", "Virginia",
		"Martha", "Debra", "Kelly", "Rachel", "Heather", "Diane", "Janet", "Samantha", "Christine", "Marie", "Julia",
		"Lauren", "Victoria", "Evelyn", "Alice", "Brenda", "Marilyn", "Carolyn", "Joyce", "Kelly", "Rose", "Ashley",
		"Nicole", "Christina", "Catherine", "Amber", "Angela", "Barbara", "Sarah", "Hannah", "Michelle", "Samantha",
		"Brittany", "Lauren", "Elizabeth", "Megan", "Jennifer", "Brittany", "Olivia", "Nicole", "Victoria", "Madison",
		"Amy", "Danielle", "Grace", "Jacqueline", "Rachel", "Leah", "Audrey", "Gabrielle", "Caroline", "Julia", "Maya",
		"Ashley", "Brooke", "Claire", "Sophia", "Isabella", "Mackenzie", "Morgan", "Taylor", "Fiona", "Charlotte", "Zoe",
		"Savannah", "Harper"};

	private final static Random RAND = new Random();
	public static String getRandomName(){
		return names[RAND.nextInt(names.length)];
	}
}
