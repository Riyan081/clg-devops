document.addEventListener('DOMContentLoaded', async () => {
  const form = document.getElementById('feedback-form');
  const routeSelect = document.getElementById('route');
  const alertContainer = document.getElementById('alert-container');

  // Load active transit routes
  try {
    const routes = await API.getRoutes();
    routeSelect.innerHTML = '<option value="">-- Select Transit Route --</option>';
    routes.forEach(r => {
      const opt = document.createElement('option');
      opt.value = r.routeCode;
      opt.textContent = `${r.routeCode} - ${r.routeName} (${r.transportMode})`;
      routeSelect.appendChild(opt);
    });
  } catch (err) {
    console.error('Failed to load routes', err);
    routeSelect.innerHTML = `
      <option value="ROUTE-101">ROUTE-101 - Downtown Express Bus (BUS)</option>
      <option value="METRO-BLUE">METRO-BLUE - Blue Line Metro (METRO)</option>
    `;
  }

  // Handle form submission
  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    alertContainer.innerHTML = '';

    const routeCode = document.getElementById('route').value;
    const category = document.getElementById('category').value;
    const passengerName = document.getElementById('passengerName').value;
    const passengerEmail = document.getElementById('passengerEmail').value;
    const description = document.getElementById('description').value;

    if (!routeCode || !category || !description.trim()) {
      alertContainer.innerHTML = `
        <div class="alert alert-danger" id="error-msg">
          Please complete all required fields (Route, Category, Description).
        </div>
      `;
      return;
    }

    try {
      const result = await API.submitFeedback({
        routeCode,
        category,
        passengerName,
        passengerEmail,
        description
      });

      alertContainer.innerHTML = `
        <div class="alert alert-success" id="success-msg">
          <strong>Success!</strong> Your feedback has been submitted successfully.<br>
          Your Tracking Ticket ID is: <strong id="ticket-id-display">${result.ticketId}</strong>
        </div>
      `;
      form.reset();
    } catch (err) {
      alertContainer.innerHTML = `
        <div class="alert alert-danger" id="error-msg">
          ${err.message || 'Error submitting feedback. Please try again.'}
        </div>
      `;
    }
  });
});
