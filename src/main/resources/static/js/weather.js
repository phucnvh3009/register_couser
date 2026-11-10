 document.addEventListener("DOMContentLoaded", function () {
  // 1. Fetch current logged in user (if any), otherwise keep default HanaYuki
  fetch("/api/me")
    .then(function (res) {
      return res.ok ? res.json() : Promise.reject(res.status);
    })
    .then(function (user) {
      if (user && user.name) {
        const greetingEl = document.getElementById("userGreetingName");
        if (greetingEl) {
          greetingEl.textContent = user.name;
        }
      }
    })
    .catch(function () {
      // Guest or not logged in, keep default "HanaYuki"
    });

  // 2. Location Data Mock
  const locationData = {
    hanoi: {
      name: "Hanoi, Vietnam",
      temp: 32,
      condition: "Partly Cloudy",
      conditionIcon: "bi-cloud-sun-fill",
      feelsLike: 36,
      humidity: "68%",
      wind: "12 km/h",
      pressure: "1008 hPa",
      visibility: "10 km",
      aqi: 42,
      aqiStatus: "Good",
      aqiColor: "#22c55e",
      pm25: "18 µg/m³",
      pm10: "28 µg/m³",
      o3: "32 ppb",
      no2: "12 ppb"
    },
    danang: {
      name: "Da Nang, Vietnam",
      temp: 29,
      condition: "Sunny",
      conditionIcon: "bi-sun-fill",
      feelsLike: 31,
      humidity: "62%",
      wind: "16 km/h",
      pressure: "1010 hPa",
      visibility: "12 km",
      aqi: 28,
      aqiStatus: "Excellent",
      aqiColor: "#10b981",
      pm25: "11 µg/m³",
      pm10: "19 µg/m³",
      o3: "25 ppb",
      no2: "8 ppb"
    },
    hcm: {
      name: "Ho Chi Minh City, Vietnam",
      temp: 31,
      condition: "Cloudy",
      conditionIcon: "bi-cloud-fill",
      feelsLike: 35,
      humidity: "75%",
      wind: "10 km/h",
      pressure: "1006 hPa",
      visibility: "9 km",
      aqi: 55,
      aqiStatus: "Moderate",
      aqiColor: "#eab308",
      pm25: "26 µg/m³",
      pm10: "42 µg/m³",
      o3: "35 ppb",
      no2: "19 ppb"
    }
  };

  function updateWeatherUI(data) {
    const cityNameEl = document.getElementById("heroCityName");
    const tempEl = document.getElementById("heroTemp");
    const condTextEl = document.getElementById("heroConditionText");
    const condIconEl = document.getElementById("heroConditionIcon");
    const feelsEl = document.getElementById("heroFeelsLike");
    const humidityEl = document.getElementById("heroHumidity");
    const windEl = document.getElementById("heroWind");
    const pressureEl = document.getElementById("heroPressure");
    const visibilityEl = document.getElementById("heroVisibility");
    const aqiNumberEl = document.getElementById("aqiNumber");
    const aqiStatusEl = document.getElementById("aqiStatus");
    const aqiProgressEl = document.getElementById("aqiProgress");

    if (cityNameEl) cityNameEl.textContent = data.name;
    if (tempEl) tempEl.textContent = data.temp + "°C";
    if (condTextEl) condTextEl.textContent = data.condition;
    if (condIconEl) {
      condIconEl.className = "hero-condition-icon bi " + data.conditionIcon;
    }
    if (feelsEl) feelsEl.textContent = "Feels like " + data.feelsLike + "°C";
    if (humidityEl) humidityEl.textContent = data.humidity;
    if (windEl) windEl.textContent = data.wind;
    if (pressureEl) pressureEl.textContent = data.pressure;
    if (visibilityEl) visibilityEl.textContent = data.visibility;

    if (aqiNumberEl) aqiNumberEl.textContent = data.aqi;
    if (aqiStatusEl) {
      aqiStatusEl.textContent = data.aqiStatus;
      aqiStatusEl.style.color = data.aqiColor;
    }
    if (aqiProgressEl) {
      aqiProgressEl.style.stroke = data.aqiColor;
      const offset = 283 - (data.aqi / 100) * 283;
      aqiProgressEl.style.strokeDashoffset = Math.max(offset, 40);
    }
  }

  // Quick Chips click
  const cityChips = document.querySelectorAll(".city-chip");
  cityChips.forEach(function (chip) {
    chip.addEventListener("click", function () {
      cityChips.forEach(c => c.classList.remove("active"));
      chip.classList.add("active");
      const key = chip.getAttribute("data-city");
      if (locationData[key]) {
        updateWeatherUI(locationData[key]);
      }
    });
  });

  // Favorite Items click
  const favItems = document.querySelectorAll(".fav-item");
  favItems.forEach(function (item) {
    item.addEventListener("click", function (e) {
      if (e.target.closest(".fav-star")) return; // Don't trigger city switch on star click
      favItems.forEach(f => f.classList.remove("active"));
      item.classList.add("active");
      const key = item.getAttribute("data-city");
      if (locationData[key]) {
        updateWeatherUI(locationData[key]);
        // Also highlight matching chip
        cityChips.forEach(c => {
          c.classList.toggle("active", c.getAttribute("data-city") === key);
        });
      }
    });
  });

  // Star Toggle
  const stars = document.querySelectorAll(".fav-star");
  stars.forEach(function (star) {
    star.addEventListener("click", function (e) {
      e.stopPropagation();
      const icon = star.querySelector("i");
      if (icon.classList.contains("bi-star-fill")) {
        icon.classList.remove("bi-star-fill");
        icon.classList.add("bi-star");
        star.classList.remove("active");
      } else {
        icon.classList.remove("bi-star");
        icon.classList.add("bi-star-fill");
        star.classList.add("active");
      }
    });
  });

  // Search Form Submit
  const searchForm = document.getElementById("searchForm");
  const searchInput = document.getElementById("searchInput");
  if (searchForm && searchInput) {
    searchForm.addEventListener("submit", function (e) {
      e.preventDefault();
      const query = searchInput.value.trim().toLowerCase();
      if (!query) return;

      if (query.includes("hanoi") || query.includes("ha noi")) {
        updateWeatherUI(locationData.hanoi);
      } else if (query.includes("da nang") || query.includes("danang")) {
        updateWeatherUI(locationData.danang);
      } else if (query.includes("ho chi minh") || query.includes("saigon") || query.includes("hcm")) {
        updateWeatherUI(locationData.hcm);
      } else {
        // Generic custom city mock
        updateWeatherUI({
          name: searchInput.value.trim(),
          temp: 30,
          condition: "Sunny Intervals",
          conditionIcon: "bi-cloud-sun-fill",
          feelsLike: 33,
          humidity: "65%",
          wind: "14 km/h",
          pressure: "1009 hPa",
          visibility: "10 km",
          aqi: 38,
          aqiStatus: "Good",
          aqiColor: "#22c55e",
          pm25: "15 µg/m³",
          pm10: "24 µg/m³",
          o3: "29 ppb",
          no2: "10 ppb"
        });
      }
    });
  }

  // Hourly Forecast scroll buttons
  const hourlyContainer = document.getElementById("hourlyScrollContainer");
  const prevBtn = document.getElementById("prevHourlyBtn");
  const nextBtn = document.getElementById("nextHourlyBtn");

  if (prevBtn && hourlyContainer) {
    prevBtn.addEventListener("click", function () {
      hourlyContainer.scrollBy({ left: -160, behavior: "smooth" });
    });
  }
  if (nextBtn && hourlyContainer) {
    nextBtn.addEventListener("click", function () {
      hourlyContainer.scrollBy({ left: 160, behavior: "smooth" });
    });
  }

  // Weather tips carousel dots
  const tips = [
    {
      title: "Carry an umbrella today",
      text: "There is a chance of light rain in the afternoon."
    },
    {
      title: "High UV Index today",
      text: "Wear sunscreen and sunglasses when outdoors between 11 AM - 3 PM."
    },
    {
      title: "Stay hydrated",
      text: "Drink at least 2 liters of water in humid weather conditions."
    },
    {
      title: "Good air quality",
      text: "Great conditions for outdoor morning workouts and jogging."
    }
  ];

  const tipTitleEl = document.getElementById("tipTitle");
  const tipTextEl = document.getElementById("tipText");
  const dots = document.querySelectorAll(".carousel-dot");

  dots.forEach(function (dot) {
    dot.addEventListener("click", function () {
      const idx = parseInt(dot.getAttribute("data-index"), 10);
      dots.forEach(d => d.classList.remove("active"));
      dot.classList.add("active");
      if (tips[idx]) {
        if (tipTitleEl) tipTitleEl.textContent = tips[idx].title;
        if (tipTextEl) tipTextEl.textContent = tips[idx].text;
      }
    });
  });
});
