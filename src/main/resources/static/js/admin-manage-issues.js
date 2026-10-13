// Auth Guard
if (sessionStorage.getItem('ptfp_admin_logged_in') !== 'true') {
  window.location.href = '/admin/login.html';
}

function adminLogout() {
  sessionStorage.removeItem('ptfp_admin_logged_in');
  sessionStorage.removeItem('ptfp_admin_user');
  localStorage.removeItem('ptfp_admin_logged_in');
  window.location.href = '/admin/login.html';
}

document.addEventListener('DOMContentLoaded', () => {
  const tableBody = document.getElementById('feedback-table-body');
  const statusFilter = document.getElementById('filter-status');
  const categoryFilter = document.getElementById('filter-category');
  const searchInput = document.getElementById('search-input');
  const searchBtn = document.getElementById('search-btn');

  async function loadFeedbacks() {
    tableBody.innerHTML = '<tr><td colspan="8" style="text-align:center;">Loading all grievances...</td></tr>';
    try {
      const items = await API.searchFeedbacks(
        statusFilter.value,
        categoryFilter.value,
        searchInput.value
      );

      if (items.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="8" style="text-align:center;color:#64748b;">No feedback records found.</td></tr>';
        return;
      }

      tableBody.innerHTML = items.map(item => `
        <tr id="row-${item.id}">
          <td><strong>${item.ticketId}</strong></td>
          <td>${item.routeCode}</td>
          <td>${item.category}</td>
          <td style="font-size:0.85rem;">
            ${item.passengerName || 'Anonymous'}
            ${item.passengerEmail ? '<br><small style="color:var(--text-muted);">' + item.passengerEmail + '</small>' : ''}
          </td>
          <td>${item.description}</td>
          <td>
            <span class="badge badge-${item.status}" id="status-badge-${item.id}">
              ${item.status}
            </span>
          </td>
          <td>${new Date(item.createdAt).toLocaleDateString()}</td>
          <td>
            <select class="form-control" style="padding:0.25rem 0.4rem;font-size:0.8rem;display:inline-block;width:auto;"
                    id="status-select-${item.id}"
                    onchange="changeStatus(${item.id}, this.value)">
              <option value="SUBMITTED" ${item.status === 'SUBMITTED' ? 'selected' : ''}>SUBMITTED</option>
              <option value="IN_REVIEW" ${item.status === 'IN_REVIEW' ? 'selected' : ''}>IN_REVIEW</option>
              <option value="RESOLVED" ${item.status === 'RESOLVED' ? 'selected' : ''}>RESOLVED</option>
              <option value="CLOSED" ${item.status === 'CLOSED' ? 'selected' : ''}>CLOSED</option>
            </select>
          </td>
        </tr>
      `).join('');
    } catch (err) {
      tableBody.innerHTML = `<tr><td colspan="8" style="color:red;text-align:center;">${err.message}</td></tr>`;
    }
  }

  window.changeStatus = async function(id, newStatus) {
    const remarks = prompt('Enter resolution remarks (optional):') || 'Status updated via admin console';
    try {
      const updated = await API.updateStatus(id, newStatus, remarks);
      const badge = document.getElementById(`status-badge-${id}`);
      if (badge) {
        badge.className = `badge badge-${updated.status}`;
        badge.textContent = updated.status;
      }
    } catch (err) {
      alert('Error updating status: ' + err.message);
      loadFeedbacks();
    }
  };

  searchBtn.addEventListener('click', loadFeedbacks);
  statusFilter.addEventListener('change', loadFeedbacks);
  categoryFilter.addEventListener('change', loadFeedbacks);
  searchInput.addEventListener('keyup', (e) => { if (e.key === 'Enter') loadFeedbacks(); });

  loadFeedbacks();
});
