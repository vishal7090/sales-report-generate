# Sales Report Generate

A Spring Boot application for automated sales report ingestion, processing, and analytics from Excel files, with MySQL persistence and a modern REST API (OpenAPI 3/Swagger UI).

## Features
- **Automated Excel Import:** Watches a folder and processes new `.xlsx` files automatically using Spring Batch.
- **Multi-Sheet Support:** Handles multiple sheets (e.g., Sales Report, Item Details) per file, with per-sheet tracking.
- **Flexible Date Parsing:** Supports multiple date formats in Excel (ISO, dd/MM/yyyy, MM/dd/yyyy).
- **MySQL Persistence:** Stores sales, item, and report tracking data in a normalized schema.
- **REST API:**
  - Filter sales and item data by date, party, product, and date range
  - Analytics endpoints: top/bottom parties, product-wise sales, month-on-month growth
  - Distinct party and product lists
  - All endpoints documented with Swagger/OpenAPI
- **Spring Batch:** Robust, production-ready batch job management and tracking.
- **Audit Tracking:** All entities have created/modified timestamps.
- **Efficient Indexing:** Database indexes for fast queries.

## Quick Start

### Prerequisites
- Java 17 or higher
- MySQL 8+
- Maven

### Setup
1. **Clone the repository:**
   ```sh
   git clone https://github.com/vishal7090/sales-report-generate.git
   cd sales-report-generate
   ```
2. **Configure the database and Excel folder:**
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/salesdb?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
   spring.datasource.username=youruser
   spring.datasource.password=yourpassword
   excel.folder-path=C:\\Work\\Report
   ```
3. **Build and run:**
   ```sh
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

### Usage
- Place `.xlsx` files in the configured folder. The app will process them automatically.
- Access the API documentation at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## API Endpoints

### Sales & Item Reports
- `GET /api/reports/sales/filter` — Filter sales by date, party, range, etc.
- `GET /api/reports/items/filter` — Filter items by date, party, product, etc.
- `GET /api/reports/sales/party-names` — List all party names
- `GET /api/reports/items/product-names` — List all product names

### Analytics
- `GET /api/report-track/summary` — Top/bottom parties, product-wise sales, month-on-month growth (parallelized)

### Report Track
- Tracks each file and sheet processed, with status and error details.

## Excel Format
- **Sales Report Sheet:**
  - Columns: Date, Invoice No, Party Name, Transaction Type, Total Amount, Payment Type, Received/Paid Amount, Balance Due
- **Item Details Sheet:**
  - Columns: Date, Invoice No./Txn No., Party Name, Item Name, Item Code, HSN/SAC, Category, Count, Challan/Order No., Quantity, Unit, UnitPrice, Discount Percent, Discount, Tax Percent, Tax, Transaction Type, Amount

## Contribution
1. Fork the repo and create your feature branch (`git checkout -b feature/fooBar`)
2. Commit your changes (`git commit -am 'Add some fooBar'`)
3. Push to the branch (`git push origin feature/fooBar`)
4. Create a new Pull Request

## License
MIT

---

For more details, see the [GitHub repository](https://github.com/vishal7090/sales-report-generate).
