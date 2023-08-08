import java.io.*;
import java.util.*;

class PersonalizedHealthAssistant {
    private static final Map<String, String> userCredentials = new HashMap<>();
    private static final Map<String, Map<String, List<String>>> userActivities = new HashMap<>();
    private static final String CREDENTIALS_FILE = "user_credentials.txt";
    private static final String ACTIVITIES_FILE = "user_activities.txt";
    private static String currentUsername = null;

    public static void main(String[] args) {
        loadUserCredentials();
        loadUserActivities();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to your Personalized Health Assistant!");

        while (true) {
            System.out.println("1. Create an account");
            System.out.println("2. Log in");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1/2/3): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> {
                    if (logIn()) {
                        showMainMenu();
                    } else {
                        System.out.println("Login failed. Please check your credentials.");
                    }
                }
                case 3 -> {
                    saveUserCredentials();
                    saveUserActivities();
                    System.out.println("Thank you for using the Personalized Health Assistant. Goodbye!");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private static void loadUserCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    userCredentials.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading user credentials: " + e.getMessage());
        }
    }

    private static void loadUserActivities() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ACTIVITIES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 4) {
                    String username = parts[0];
                    String password = parts[1];
                    String date = parts[2];
                    List<String> activities = new ArrayList<>(Arrays.asList(parts).subList(3, parts.length));
                    userActivities
                            .computeIfAbsent(username, k -> new HashMap<>())
                            .computeIfAbsent(password, k -> new HashMap<>())
                            .computeIfAbsent(date, k -> new ArrayList<>())
                            .addAll(activities);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading user activities: " + e.getMessage());
        }
    }

    private static void createAccount() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please create a username: ");
        String username = scanner.nextLine();

        System.out.print("Please create a password: ");
        String password = scanner.nextLine();

        userCredentials.put(username, password);

        System.out.println("Account created successfully. You can now log in.");
    }

    private static boolean logIn() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Username: ");
        String inputUsername = scanner.nextLine();

        System.out.print("Password: ");
        String inputPassword = scanner.nextLine();

        return authenticateUser(inputUsername, inputPassword);
    }

    private static boolean authenticateUser(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    private static void showMainMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Login successful!");

        while (true) {
            System.out.println("Hello! How can I assist you today?");
            System.out.println("1. Track your daily activities");
            System.out.println("2. Monitor your health metrics");
            System.out.println("3. Get personalized health recommendations");
            System.out.println("4. Display activities");
            System.out.println("5. Log out");
            System.out.print("Enter your choice (1/2/3/4/5): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> trackActivities();
                case 2 -> monitorHealthMetrics();
                case 3 -> getHealthRecommendations();
                case 4 -> displayActivities();
                case 5 -> {
                    saveUserActivities();
                    System.out.println("Logged out. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private static void trackActivities() {
        Scanner scanner = new Scanner(System.in);

        String username = getCurrentUsername();

        System.out.print("Enter the date (MM/DD/YYYY): ");
        String date = scanner.nextLine();

        System.out.println("Activity Tracking for " + username + " on " + date);
        System.out.println("Enter your activities for today (type 'done' to finish):");

        String activity;
        do {
            System.out.print("> ");
            activity = scanner.nextLine();

            if (!activity.equalsIgnoreCase("done")) {
                userActivities
                        .computeIfAbsent(username, k -> new HashMap<>())
                        .computeIfAbsent(getCurrentUserPassword(), k -> new HashMap<>())
                        .computeIfAbsent(date, k -> new ArrayList<>())
                        .add(activity);
            }
        } while (!activity.equalsIgnoreCase("done"));

        System.out.println("Activities tracked:");
        List<String> activities = userActivities
                .get(username)
                .get(getCurrentUserPassword())
                .get(date);
        if (activities != null) {
            for (int i = 0; i < activities.size(); i++) {
                System.out.println((i + 1) + ". " + activities.get(i));
            }
        }
    }

    private static void monitorHealthMetrics() {
        Scanner scanner = new Scanner(System.in);

        String username = getCurrentUsername();

        System.out.println("Health Metrics Monitoring for " + username);

        System.out.print("Enter your weight (in kg): ");
        double weight = scanner.nextDouble();

        System.out.print("Enter your height (in cm): ");
        double height = scanner.nextDouble();

        double bmi = calculateBMI(weight, height);
        String bmiCategory = getBMICategory(bmi);

        System.out.println("Your BMI: " + bmi);
        System.out.println("BMI Category: " + bmiCategory);
    }

    private static double calculateBMI(double weight, double height) {
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }

    private static String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi >= 18.5 && bmi < 24.9) {
            return "Normal Weight";
        } else if (bmi >= 25.0 && bmi < 29.9) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    private static void getHealthRecommendations() {
        Scanner scanner = new Scanner(System.in);

        String username = getCurrentUsername();

        System.out.println("Personalized Health Recommendations for " + username);

        System.out.println("Please answer a few questions to provide personalized recommendations:");

        System.out.print("Are you trying to lose weight? (yes/no): ");
        String weightLossGoal = scanner.nextLine().toLowerCase();

        System.out.print("Do you have any dietary restrictions? (yes/no): ");
        String dietaryRestrictions = scanner.nextLine().toLowerCase();

        System.out.print("How many hours of physical activity do you engage in per week? ");
        int weeklyActivityHours = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Please enter your age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Do you smoke? (yes/no): ");
        String smokingStatus = scanner.nextLine().toLowerCase();

        System.out.print("How many hours of sleep do you typically get per night? ");
        int sleepHours = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Here are your personalized health recommendations:");

        if (weightLossGoal.equals("yes")) {
            System.out.println("- Focus on a balanced diet and regular exercise.");
            System.out.println("- Consider reducing calorie intake and avoiding sugary snacks.");
        } else {
            System.out.println("- Maintain a balanced diet to support your overall health.");
        }

        if (dietaryRestrictions.equals("yes")) {
            System.out.println("- Consult with a nutritionist to create a suitable meal plan.");
            System.out.println("- Ensure you are getting all essential nutrients through alternate sources.");
        }

        if (weeklyActivityHours < 3) {
            System.out.println("- Aim to increase your physical activity to at least 150 minutes per week.");
            System.out.println("- Incorporate activities you enjoy, such as walking, cycling, or swimming.");
        } else if (weeklyActivityHours < 5) {
            System.out.println("- Keep up the good work with your physical activity routine!");
            System.out.println("- Consider adding variety to your workouts for better overall fitness.");
        } else {
            System.out.println("- Your dedication to regular physical activity is commendable!");
            System.out.println("- Continue with your exercise routine and stay hydrated.");
        }

        if (age >= 18 && age <= 30) {
            System.out.println("- Make sure to include strength training in your exercise routine for bone health.");
        } else if (age > 30 && age <= 50) {
            System.out.println("- Prioritize cardiovascular exercises to maintain heart health.");
        } else if (age > 50) {
            System.out.println("- Focus on balance and stability exercises to prevent falls.");
            System.out.println("- Consider adding yoga or tai chi to your routine for flexibility and mindfulness.");
        }

        if (smokingStatus.equals("yes")) {
            System.out.println("- Quitting smoking can significantly improve your lung and heart health.");
            System.out.println("- Seek support from healthcare professionals or support groups to quit.");
        }

        if (sleepHours < 6) {
            System.out.println("- Aim for more sleep to support cognitive function and overall well-being.");
            System.out.println("- Create a relaxing bedtime routine to improve sleep quality.");
        } else if (sleepHours >= 8) {
            System.out.println("- You're getting a good amount of sleep! Keep up the healthy sleep pattern.");
        }

        System.out.println("- Stay hydrated and aim for 7-8 hours of sleep per night.");
        System.out.println("- Consider incorporating more fruits and vegetables into your meals.");
        System.out.println("- Practice stress-reduction techniques such as meditation or deep breathing.");
        System.out.println("- Limit processed foods and choose whole, nutrient-rich options.");
        System.out.println("- Stay connected with friends and family to support your emotional well-being.");
        System.out.println("- Regularly schedule check-ups with your healthcare provider.");
    }

    private static void displayActivities() {
        Scanner scanner = new Scanner(System.in);

        String username = getCurrentUsername();

        System.out.print("Enter the password: ");
        String password = scanner.nextLine();

        if (userActivities.containsKey(username) && userActivities.get(username).containsKey(password)) {
            System.out.println("Display Activities for " + username);
            System.out.print("Enter the date (MM/DD/YYYY): ");
            String date = scanner.nextLine();

            if (userActivities.get(username).get(password).containsKey(date)) {
                List<String> activities = userActivities.get(username).get(password).get(date);
                System.out.println("Activities tracked for " + username + " on " + date + ":");
                for (int i = 0; i < activities.size(); i++) {
                    System.out.println((i + 1) + ". " + activities.get(i));
                }
            } else {
                System.out.println("No activities tracked for " + username + " on " + date + ".");
            }
        } else {
            System.out.println("No activities tracked for " + username + ".");
        }
    }

    private static String getCurrentUsername() {
        if (currentUsername == null) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your Name: ");
            currentUsername = scanner.nextLine();
        }
        return currentUsername;
    }

    private static String getCurrentUserPassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your Password: ");
        return scanner.nextLine();
    }

    private static void saveUserCredentials() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CREDENTIALS_FILE))) {
            for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error saving user credentials: " + e.getMessage());
        }
    }

    private static void saveUserActivities() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACTIVITIES_FILE))) {
            for (Map.Entry<String, Map<String, List<String>>> userEntry : userActivities.entrySet()) {
                String username = userEntry.getKey();
                for (Map.Entry<String, List<String>> passwordEntry : userEntry.getValue().entrySet()) {
                    String password = passwordEntry.getKey();
                    for (Map.Entry<String, List<String>> activitiesEntry : passwordEntry.getValue().entrySet()) {
                        String date = activitiesEntry.getKey();
                        List<String> activities = activitiesEntry.getValue();
                        writer.print(username + ":" + password + ":" + date);
                        for (String activity : activities) {
                            writer.print(":" + activity);
                        }
                        writer.println();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving user activities: " + e.getMessage());
        }
    }
}
