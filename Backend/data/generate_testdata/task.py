import random
from faker import Faker

# Initialize Faker
fake = Faker()

# Generate random task records
task_records = []
for _ in range(10000):
    owner_id = random.randint(1, 10000)  # Assuming owner_id ranges from 1 to 10000
    title = fake.sentence(nb_words=6)[:32].replace('"', '""')  # Ensure title does not exceed 32 characters
    images = " ".join([fake.image_url() for _ in range(random.randint(0, 3))]).replace('"', '""')
    description = fake.text(max_nb_chars=255).replace('"', '""')  # Ensure description does not exceed 255 characters
    price = random.randint(1, 10000) * 100
    created_at = fake.date_time_between(start_date='-1y', end_date='now').strftime('%Y-%m-%d %H:%M:%S')
    max_access = random.randint(1, 10)
    rating = random.randint(0, 100)
    collected = random.randint(0, 10000)
    status = random.choice([0, 2])

    task_records.append(
        f"({owner_id}, \"{title}\", \"{images}\", \"{description}\", {price}, '{created_at}', {max_access}, {rating}, {collected}, {status})")

# Create SQL insert statement
sql_insert_statement = "INSERT INTO task (owner_id, title, images, description, price, created_at, max_access, rating, collected, status) VALUES\n"

# Write to the file in chunks to handle large data
with open("insert_task.sql", "w", encoding="utf-8") as file:
    file.write(sql_insert_statement)
    for i in range(0, len(task_records), 1000):  # Write 1000 records at a time
        chunk = ",\n".join(task_records[i:i + 1000]) + ";\n"
        if i + 1000 < len(task_records):
            chunk = chunk[:-2] + ",\n"
        file.write(chunk)

print("SQL insert script has been generated and saved to insert_task.sql")
