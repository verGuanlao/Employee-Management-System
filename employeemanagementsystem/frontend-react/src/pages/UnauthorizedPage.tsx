import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import DashboardLayout from '@/layouts/DashboardLayout';
import { useNavigate } from 'react-router-dom';

const UnauthorizedPage = () => {
  const navigate = useNavigate();

  return (
    <DashboardLayout page={0} totalPages={1} onPageChange={() => {}}>
      <Card>
        <CardHeader className="flex items-center justify-between">
          <CardTitle>Access Denied</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex flex-col items-center justify-center gap-4 py-12 text-center">
            <span className="text-6xl">🔒</span>
            <h2 className="text-xl font-semibold text-gray-700">
              You do not have permission to view this page
            </h2>
            <p className="text-gray-500">
              This page is restricted to administrators only. Please contact your admin if you
              believe this is a mistake.
            </p>
            <button
              onClick={() => navigate('/login')}
              className="mt-4 rounded bg-primary px-6 py-2 text-white transition hover:opacity-90"
            >
              Back to Login
            </button>
          </div>
        </CardContent>
      </Card>
    </DashboardLayout>
  );
};

export default UnauthorizedPage;
