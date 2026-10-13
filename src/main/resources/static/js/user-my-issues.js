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

      tableBody.innerHTML = myItems.map(item => `
        <tr id="row-${item.id}">
          <td><strong>${item.ticketId}</strong></td>
          <td>${item.routeCode}</td>
          <td>${item.category}</td>
          <td>${item.description}</td>
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

  loadBtn.addEventListener('click', () => {
    loadMyIssues(emailInput.value);
  });

  emailInput.addEventListener('keyup', (e) => {
    if (e.key === 'Enter') loadMyIssues(emailInput.value);
  });
});
