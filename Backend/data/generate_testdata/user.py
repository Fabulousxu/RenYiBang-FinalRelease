import random
from faker import Faker

# Initialize Faker
fake = Faker()

# Define user types
user_types = [0, 1, 2]  # 0:普通用户, 1:客服, 2:管理员

# Generate random user records
user_records = []
for _ in range(10000):
    user_type = random.choice(user_types)
    nickname = fake.user_name()[:16].replace('"', '""')  # Ensure nickname does not exceed 16 characters
    avatar = fake.image_url().replace('"', '""')
    intro = fake.text(max_nb_chars=255).replace('"', '""')  # Ensure intro does not exceed 255 characters
    rating = random.randint(0, 100)
    balance = random.randint(0, 10000)  # Assuming balance ranges from 0 to 100,000
    phone = fake.phone_number()[:14].replace('"', '""')  # Ensure phone does not exceed 14 characters
    email = fake.email().replace('"', '""')
    following = random.randint(0, 1000)
    follower = random.randint(0, 1000)

    user_records.append(
        f"({user_type}, \"{nickname}\", \"{avatar}\", \"{intro}\", {rating}, {balance}, \"{phone}\", \"{email}\", {following}, {follower})")

# Create SQL insert statement
sql_insert_statement = "INSERT INTO user (type, nickname, avatar, intro, rating, balance, phone, email, following, follower) VALUES\n"

# Write to the file in chunks to handle large data
with open("insert_user.sql", "w", encoding="utf-8") as file:
    file.write(sql_insert_statement)
    for i in range(0, len(user_records), 1000):  # Write 1000 records at a time
        chunk = ",\n".join(user_records[i:i + 1000]) + ";\n"
        if i + 1000 < len(user_records):
            chunk = chunk[:-2] + ",\n"
        file.write(chunk)

print("SQL insert script has been generated and saved to insert_user.sql")
