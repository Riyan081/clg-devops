// Shared API helper using vanilla fetch()
const API = {
  async getRoutes() {
    const res = await fetch('/api/routes');
    if (!res.ok) throw new Error('Failed to fetch routes');
    return await res.json();
  },

  async submitFeedback(data) {
    const res = await fetch('/api/feedback', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    if (!res.ok) {
      const err = await res.json().catch(() => ({}));
      throw new Error(err.message || 'Failed to submit feedback');
    }
    return await res.json();
  },

  async searchFeedbacks(status = '', category = '', search = '') {
    const params = new URLSearchParams();
    if (status) params.append('status', status);
    if (category) params.append('category', category);
    if (search) params.append('search', search);

    const res = await fetch(`/api/feedback?${params.toString()}`);
    if (!res.ok) throw new Error('Failed to load feedbacks');
    return await res.json();
  },

  async updateStatus(id, status, remarks = '') {
    const res = await fetch(`/api/feedback/${id}/status`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status, resolutionRemarks: remarks })
    });
    if (!res.ok) {
      const err = await res.json().catch(() => ({}));
      throw new Error(err.message || 'Failed to update status');
    }
    return await res.json();
  },

  async getDashboardSummary() {
    const res = await fetch('/api/dashboard/summary');
    if (!res.ok) throw new Error('Failed to fetch dashboard summary');
    return await res.json();
  }
};
