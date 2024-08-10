import random
from faker import Faker

# Initialize Faker
fake = Faker()

# Generate random task_comment records
task_comment_records = []
for _ in range(10000):
    task_id = random.randint(1, 10000)  # Assuming task_id ranges from 1 to 10000
    commenter_id = random.randint(1, 10000)  # Assuming commenter_id ranges from 1 to 10000
    content = fake.text(max_nb_chars=255).replace('"', '""')  # Ensure content does not exceed 255 characters
    created_at = fake.date_time_between(start_date='-1y', end_date='now').strftime('%Y-%m-%d %H:%M:%S')
    rating = random.randint(0, 100)
    liked_number = random.randint(0, 10000)

    task_comment_records.append(f"({task_id}, {commenter_id}, \"{content}\", '{created_at}', {rating}, {liked_number})")

# Create SQL insert statement
sql_insert_statement = "INSERT INTO task_comment (task_id, commenter_id, content, created_at, rating, liked_number) VALUES\n"

# Write to the file in chunks to handle large data
with open("insert_task_comment.sql", "w", encoding="utf-8") as file:
    file.write(sql_insert_statement)
    for i in range(0, len(task_comment_records), 1000):  # Write 1000 records at a time
        chunk = ",\n".join(task_comment_records[i:i + 1000]) + ";\n"
        if i + 1000 < len(task_comment_records):
            chunk = chunk[:-2] + ",\n"
        file.write(chunk)

print("SQL insert script has been generated and saved to insert_task_comment.sql")
