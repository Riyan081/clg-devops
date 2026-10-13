document.addEventListener('DOMContentLoaded', async () => {
  const form = document.getElementById('feedback-form');
  const routeSelect = document.getElementById('route');
  const alertContainer = document.getElementById('alert-container');

  // Check Commuter Authentication
  const currentUser = API.getCurrentUser();
  if (!currentUser) {
    window.location.href = '/user/login.html?redirect=' + encodeURIComponent('/user/raise-issue.html');
    return;
  }

  // Display User badge and logout button in navbar
  const badge = document.getElementById('nav-user-badge');
  const nameEl = document.getElementById('nav-user-name');
  const logoutBtn = document.getElementById('btn-logout');
  if (badge && nameEl) {
    nameEl.textContent = currentUser.fullName;
    badge.style.display = 'inline-flex';
  }
  if (logoutBtn) {
    logoutBtn.style.display = 'inline-block';
  }

  // Autofill passenger info from authenticated commuter
  const nameInput = document.getElementById('passengerName');
  const emailInput = document.getElementById('passengerEmail');
  if (nameInput) {
    nameInput.value = currentUser.fullName || '';
    nameInput.readOnly = true;
    nameInput.style.backgroundColor = '#f1f5f9';
  }
  if (emailInput) {
    emailInput.value = currentUser.email || '';
    emailInput.readOnly = true;
    emailInput.style.backgroundColor = '#f1f5f9';
  }

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

  // Image attachment handler
  let selectedBase64Image = null;
  const imageInput = document.getElementById('issue-image');
  const previewContainer = document.getElementById('image-preview-container');
  const previewImg = document.getElementById('image-preview');
  const removeImgBtn = document.getElementById('btn-remove-image');

  if (imageInput) {
    imageInput.addEventListener('change', (e) => {
      const file = e.target.files[0];
      if (!file) return;

      if (file.size > 2 * 1024 * 1024) {
        alert('Image is too large. Please select an image under 2MB.');
        imageInput.value = '';
        return;
      }

      const reader = new FileReader();
      reader.onload = (event) => {
        selectedBase64Image = event.target.result;
        previewImg.src = selectedBase64Image;
        previewContainer.style.display = 'flex';
      };
      reader.readAsDataURL(file);
    });
  }

  if (removeImgBtn) {
    removeImgBtn.addEventListener('click', () => {
      selectedBase64Image = null;
      if (imageInput) imageInput.value = '';
      if (previewContainer) previewContainer.style.display = 'none';
      if (previewImg) previewImg.src = '';
    });
  }

  // Handle form submission
  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    alertContainer.innerHTML = '';

    const routeCode = document.getElementById('route').value;
    const category = document.getElementById('category').value;
    const passengerName = document.getElementById('passengerName').value.trim();
    const passengerEmail = document.getElementById('passengerEmail').value.trim();
    const description = document.getElementById('description').value;

    if (!routeCode || !category || !description.trim() || !passengerName || !passengerEmail) {
      alertContainer.innerHTML = `
        <div class="alert alert-danger" id="error-msg">
          Please complete all required fields (Route, Category, Name, Email, Description).
        </div>
      `;
      return;
    }

    // Save user info for future use
    localStorage.setItem('ptfp_user_name', passengerName);
    localStorage.setItem('ptfp_user_email', passengerEmail);

    try {
      const result = await API.submitFeedback({
        routeCode,
        category,
        passengerName,
        passengerEmail,
        description,
        imageUrl: selectedBase64Image
      });

      alertContainer.innerHTML = `
        <div class="alert alert-success" id="success-msg">
          <strong>Success!</strong> Your grievance has been submitted successfully.<br>
          Your Tracking Ticket ID is: <strong id="ticket-id-display">${result.ticketId}</strong><br>
          <small style="color:#15803d;">You can track this issue from the <a href="/user/my-issues.html" style="color:#1d4ed8; font-weight:600;">My Issues</a> page.</small>
        </div>
      `;
      form.reset();
      selectedBase64Image = null;
      if (previewContainer) previewContainer.style.display = 'none';
      if (previewImg) previewImg.src = '';
      // Re-fill name and email after reset
      document.getElementById('passengerName').value = passengerName;
      document.getElementById('passengerEmail').value = passengerEmail;
    } catch (err) {
      alertContainer.innerHTML = `
        <div class="alert alert-danger" id="error-msg">
          ${err.message || 'Error submitting grievance. Please try again.'}
        </div>
      `;
    }
  });
});
