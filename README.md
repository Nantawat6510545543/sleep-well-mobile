# Sleep-Well Mobile App: AI-Powered Sleep Analysis

## Team Members

1. Nantawat Sukrisunt - 6510545543
2. Naytitorn Chaovirachot - 6510545560

All team members are affiliated with the Department of Engineering, Faculty of Software and
Knowledge Engineering, and are students at Kasetsart University.

---

## Overview

**Sleep-Well** is a mobile application designed to help users understand and improve their sleep
quality using an embedded AI model. The app allows users to input personal and environmental
sleep-related data, and instantly receive a predicted sleep score along with personalized insights
and recommendations.

Key features include:

- AI-powered sleep score prediction
- Environmental data integration via WeatherAPI
- Background noise detection using the phone microphone
- Visualization of sleep trends and insights

The application is built using **Kotlin** for Android devices and stores data in **Firebase**. It is
designed to be lightweight, user-friendly, and data-driven.

---

## AI Model

The AI model was developed using Google Colab and trained to predict a sleep quality score based on
both personal and environmental factors.

👉 [View the AI Model on Google Colab](https://colab.research.google.com/drive/1XVsWzQs8yWUHJDgGk5xxtxKcujiFlw_o#scrollTo=jrynZdRIu5Vg)

---

## Model Input Features

| Feature        | Data Type |
|----------------|-----------|
| sex            | int64     |
| age            | int64     |
| height         | int64     |
| weight         | float64   |
| temp_c         | float64   |
| condition_text | int64     |
| precip_mm      | float64   |
| humidity       | float64   |
| noise          | float64   |
| sleep_duration | float64   |
| sentiment      | float64   |
