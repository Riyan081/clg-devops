// Auth Guard
if (localStorage.getItem('ptfp_admin_logged_in') !== 'true') {
  window.location.href = '/admin/login.html';
}

function adminLogout() {
  localStorage.removeItem('ptfp_admin_logged_in');
  localStorage.removeItem('ptfp_admin_user');
  window.location.href = '/admin/login.html';
}

document.addEventListener('DOMContentLoaded', async () => {
  const metricTotal = document.getElementById('metric-total');
  const metricSubmitted = document.getElementById('metric-submitted');
  const metricInReview = document.getElementById('metric-in-review');
  const metricResolved = document.getElementById('metric-resolved');
  const metricRate = document.getElementById('metric-rate');
  const breakdownContainer = document.getElementById('category-breakdown');

  try {
    const data = await API.getDashboardSummary();

    metricTotal.textContent = data.totalFeedbacks;
    metricSubmitted.textContent = data.submittedCount;
    metricInReview.textContent = data.inReviewCount;
    metricResolved.textContent = data.resolvedCount;
    metricRate.textContent = `${data.resolutionRatePercentage}%`;

    if (data.categoryDistribution) {
      breakdownContainer.innerHTML = Object.entries(data.categoryDistribution).map(([cat, count]) => {
        const pct = data.totalFeedbacks > 0 ? Math.round((count / data.totalFeedbacks) * 100) : 0;
        return `
          <div style="margin-bottom: 0.85rem;">
            <div style="display:flex; justify-content:space-between; font-size:0.9rem; font-weight:600; margin-bottom:0.25rem;">
              <span>${cat}</span>
              <span>${count} (${pct}%)</span>
            </div>
            <div style="background:#e2e8f0; border-radius:4px; height:8px; overflow:hidden;">
              <div style="background:#1d4ed8; width:${pct}%; height:100%;"></div>
            </div>
          </div>
        `;
      }).join('');
    }
  } catch (err) {
    console.error('Failed to load dashboard', err);
  }
});
