# Sleep-Well Mobile App: AI-Powered Sleep Analysis

## Team Members

1. Nantawat Sukrisunt - 6510545543
2. Naytitorn Chaovirachot - 6510545560

All team members are affiliated with the Department of Engineering, Faculty of Software and
Knowledge Engineering, and are students at Kasetsart University.

---

## Overview

**Sleep-Well Mobile App** is the **sequel** to our previous
project, [Sleep-Well RESTful API Web Service](https://github.com/Nantawat6510545543/sleep-well).  
The original project focused on collecting and analyzing sleep data through an API service.

> **Original Project Overview**:  
> Sleep-Well is a RESTful API web service designed to offer sleep quality data to users. The API
> provides endpoints for accessing sleep information, including sleep duration, quality scores,
> environmental factors (such as weather and noise levels), and personal information about an
> individual's sleep patterns. It also features analytics to analyze sleep comments and provide
> insights into sleep quality trends.

---

## Project Overview

**Sleep-Well Mobile App** extends this concept by turning the backend insights into a user-friendly
**mobile experience**. It uses a trained AI model to predict sleep scores based on personal and
environmental data, and helps users track and improve their sleep habits.

Key features include:

- AI-powered sleep score prediction
- Environmental data integration via WeatherAPI
- Background noise detection using the phone microphone
- Visualization of sleep trends and insights

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
