document.addEventListener('DOMContentLoaded', () => {
  const tableBody = document.getElementById('my-issues-table-body');
  const emailInput = document.getElementById('user-email-input');
  const loadBtn = document.getElementById('load-my-issues-btn');
  const userInfoBar = document.getElementById('user-info-bar');
  const currentUserEmail = document.getElementById('current-user-email');
  const issueCountLabel = document.getElementById('issue-count-label');

  // Check Commuter Authentication
  const currentUser = API.getCurrentUser();
  if (!currentUser) {
    window.location.href = '/user/login.html?redirect=' + encodeURIComponent('/user/my-issues.html');
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

  // Auto-fill and load issues for logged-in user
  if (currentUser.email) {
    emailInput.value = currentUser.email;
    loadMyIssues(currentUser.email);
  }

  async function loadMyIssues(email) {
    if (!email || !email.trim()) {
      tableBody.innerHTML = '<tr><td colspan="7" style="text-align:center; color:var(--text-muted);">Please enter your email above.</td></tr>';
      userInfoBar.style.display = 'none';
      return;
    }

    tableBody.innerHTML = '<tr><td colspan="7" style="text-align:center;">Loading your issues...</td></tr>';

    try {
      // Fetch all feedbacks and filter client-side by email
      const allItems = await API.searchFeedbacks('', '', '');
      const myItems = allItems.filter(item =>
        item.passengerEmail && item.passengerEmail.toLowerCase() === email.trim().toLowerCase()
      );

      // Save email for future
      localStorage.setItem('ptfp_user_email', email.trim());

      // Show user info bar
      userInfoBar.style.display = 'block';
      currentUserEmail.textContent = email.trim();
      issueCountLabel.textContent = `${myItems.length} issue(s) found`;

      if (myItems.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="7" style="text-align:center;color:#64748b;">No issues found for this email. Submit a grievance from the Raise Issue page.</td></tr>';
        return;
      }

      window.myIssuesMap = {};
      myItems.forEach(i => { window.myIssuesMap[i.id] = i; });

      tableBody.innerHTML = myItems.map(item => `
        <tr id="row-${item.id}">
          <td><strong>${item.ticketId}</strong></td>
          <td>${item.routeCode}</td>
          <td>${item.category}</td>
          <td>
            <div>${item.description}</div>
            ${item.imageUrl ? `
              <div style="margin-top:6px;">
                <button type="button" class="btn" style="padding:0.25rem 0.5rem; font-size:0.75rem; background:#eff6ff; color:#1d4ed8; border:1px solid #bfdbfe; cursor:pointer;" onclick="viewMyPhoto(${item.id})">
                  📷 View Photo
                </button>
              </div>
            ` : ''}
          </td>
          <td>
            <span class="badge badge-${item.status}">
              ${item.status}
            </span>
          </td>
          <td>${new Date(item.createdAt).toLocaleDateString()}</td>
          <td style="font-size:0.85rem; color:var(--text-muted);">
            ${item.resolutionRemarks ? item.resolutionRemarks : '<em>Pending</em>'}
          </td>
        </tr>
      `).join('');
    } catch (err) {
      tableBody.innerHTML = `<tr><td colspan="7" style="color:red;text-align:center;">${err.message}</td></tr>`;
    }
  }

  window.viewMyPhoto = function(id) {
    const item = window.myIssuesMap[id];
    if (!item || !item.imageUrl) return;
    const modal = document.getElementById('photo-modal');
    const modalImg = document.getElementById('modal-photo-img');
    const modalTitle = document.getElementById('modal-photo-title');
    if (modal && modalImg) {
      modalImg.src = item.imageUrl;
      if (modalTitle) modalTitle.textContent = `Attached Photo — Ticket: ${item.ticketId}`;
      modal.style.display = 'flex';
    }
  };

  window.closeMyPhotoModal = function() {
    const modal = document.getElementById('photo-modal');
    if (modal) modal.style.display = 'none';
  };

  loadBtn.addEventListener('click', () => {
    loadMyIssues(emailInput.value);
  });

  emailInput.addEventListener('keyup', (e) => {
    if (e.key === 'Enter') loadMyIssues(emailInput.value);
  });
});
