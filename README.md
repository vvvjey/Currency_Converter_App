# Currency Exchange Rate Viewer

## A Brief App Overview
The **Currency Exchange Rate Viewer** is a mobile application that allows users to:
- 📌 Select two currencies (base currency and target currency).
- 📈 View the historical exchange rate trends over the past year in a visually appealing **line chart**.
- 🛡️ Error handling for invalid inputs or network failures.

The app follows the **Model-View-Controller (MVC)** architecture to ensure clean code structure and maintainability.

The app leverages:
- **Retrofit**: For fetching exchange rate data from an external API.
- **MPAndroidChart**: For displaying the historical trends in a line chart.
- **Exchangerate.host API** : For fetching currency rate API

<p align="center">
  <img src="https://github.com/user-attachments/assets/8d44e2d0-f230-4254-8169-b4bd9bf7836a" width="30%" />
  <img src="https://github.com/user-attachments/assets/1e9e6f90-e402-4d68-acb5-0e67618a9a37" width="30%" />
  <img src="https://github.com/user-attachments/assets/09c0d992-5e49-40ed-b1dc-26bca731f914" width="30%" />
</p>



This project serves as a demonstration of clean architecture, third-party library integration, and API data visualization for mobile applications. It is developed as part of the **TymeX Challenge Round 2**.

---

## Steps to Build and Run It

### 1. Prerequisites
Ensure that the following tools and software are installed:
- **Android Studio** (latest version)
- **JDK 11 or higher**
- **Gradle** (comes with Android Studio)
- A mobile device or Android emulator for testing

### 2. Clone the Repository
Clone the repository from GitHub to your local machine:
```bash
git clone https://github.com/your-username/currency-exchange-viewer.git
cd currency-exchange-viewer
```


### 3. Open the Project in Android Studio
- Launch **Android Studio**.
- Click on **Open** and navigate to the folder where you cloned the repository.


Sync the project to download the required libraries.

### 4. Build and Run the App
- Connect an Android device via USB or launch an emulator.
- Click on **Run** (Shift + F10) in Android Studio to build and deploy the app.

### 5. App Usage
1. Select the **Base Currency** and **Target Currency**.
2. The app fetches the historical exchange rates for the past year.
3. A **Line Chart** displays the trends visually.
4. 🚨 Handles errors gracefully, such as network issues or invalid inputs.

---

## Key Features
- 🎯 Clean and simple user interface for selecting currencies.
- 🌐 Integration with a real-time exchange rate API.
- 📊 Line chart visualization using the **MPAndroidChart** library.
- ⚙️ **MVC Architecture** for well-structured and maintainable code.
- 🛡️ Error handling for network failures or incorrect inputs.

---

## Technologies Used
- **Kotlin**: Primary programming language
- **Retrofit**: For REST API calls
- **MPAndroidChart**: To render the line chart
- **Android Studio**: Development environment

---

## Future Enhancements
- Add support for date range selection.
- Implement caching for offline access.
- Improve UI with animations and interactivity.
- Add more visual chart types (e.g., bar chart, candlestick chart).


## Quick Note:
🚀 **Developed in just 1 day** for the TymeX Challenge Round 2!

---

**Challenge Round 2 Submission** - Developed by Hoàng Văn Tú - hoangtu4520031234@gmail.com for TymeX. 🌟

---

📈 📊 💡
