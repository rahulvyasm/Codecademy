'''Machine Learning and AI Fundamentals Skill Path > Python Fundamentals for ML/AI (Part I) > Python Functions > Python Functions: Medical Insurance Project'''
# Create calculate_insurance_cost() function below: 
def calculate_insurance_cost(age, sex, bmi, num_of_children, smoker, name):
  estimated_cost = 250*age - 128*sex + 370*bmi + 425*num_of_children + 24000*smoker - 12500
  print("The estimated insurance cost for " + name + " is " + str(estimated_cost) + " dollars.")
  return estimated_cost

# Initial variables for Maria 

# Estimate Maria's insurance cost
insurance_cost = calculate_insurance_cost(28, 0, 26.2, 3, 0, 'Maria')

# Initial variables for Omar

# Estimate Omar's insurance cost 
insurance_cost = calculate_insurance_cost(35, 1, 22.2, 0, 1, 'Omar')
