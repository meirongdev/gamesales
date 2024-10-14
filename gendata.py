import csv
import random
import datetime
import time
from decimal import Decimal, ROUND_HALF_UP

def generate_csv_data(filename, num_rows):
    with open(filename, 'w', newline='') as csvfile:
        fieldnames = ['id', 'game_no', 'game_name', 'game_code', 'type', 'cost_price', 'tax', 'sale_price', 'date_of_sale']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

        writer.writeheader()

        for i in range(1, num_rows + 1):
            # Generate a random date between 1 April and 30 April
            start_date = datetime.datetime(year=2024, month=4, day=1)
            end_date = datetime.datetime(year=2024, month=4, day=30)
            random_date = start_date + datetime.timedelta(days=random.randint(0, (end_date - start_date).days))

            cost_price = Decimal(random.uniform(0, 100)).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)
            sale_price = (cost_price * Decimal('1.09')).quantize(Decimal('0.01'), rounding=ROUND_HALF_UP)


            row = {
                'id': i,
                'game_no': random.randint(1, 100),
                'game_name': ''.join(random.choices('abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ', k=random.randint(1, 20))),
                'game_code': ''.join(random.choices('abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', k=5)),
                'type': random.choice([1, 2]),
                'cost_price': cost_price,
                'tax': Decimal('0.09'),
                'sale_price': sale_price,
                'date_of_sale': random_date.strftime('%Y-%m-%d %H:%M:%S')
            }
            writer.writerow(row)

if __name__ == '__main__':
    filename = 'game_data.csv'
    num_rows = 1000000
    generate_csv_data(filename, num_rows)
    print(f"Generated {num_rows} rows of CSV data to {filename}")