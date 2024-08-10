# Generate auth records with user_id from 1 to 10000 and password as '123'
auth_records = []
for user_id in range(1, 10001):  # Assuming user_id ranges from 1 to 10000
    password = "123"
    auth_records.append(f"({user_id}, \"{password}\")")

# Create SQL insert statement
sql_insert_statement = "INSERT INTO auth (user_id, password) VALUES\n"

# Write to the file in chunks to handle large data
with open("insert_user_auth.sql", "w", encoding="utf-8") as file:
    file.write(sql_insert_statement)
    for i in range(0, len(auth_records), 1000):  # Write 1000 records at a time
        chunk = ",\n".join(auth_records[i:i+1000]) + ";\n"
        if i + 1000 < len(auth_records):
            chunk = chunk[:-2] + ",\n"
        file.write(chunk)

print("SQL insert script has been generated and saved to insert_user_auth.sql")
