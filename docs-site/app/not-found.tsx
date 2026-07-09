import Link from "next/link";
import { FpIcon } from "@/components/fp-icon";
import { Button } from "@/components/ui/button";
import { Home } from "lucide-react";

export default function NotFound() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-6 bg-white px-6 py-16">
      <div className="w-full max-w-sm rounded-xl border border-gray-200 bg-white p-10 text-center shadow-sm">
        <FpIcon className="mx-auto size-12" />
        <h1 className="mt-6 text-xl font-bold text-gray-900">Page not found</h1>
        <p className="mt-2 text-sm text-gray-500">The page you requested does not exist.</p>
        <Button className="mt-8" variant="primary" asChild>
          <Link href="/"><Home />Back to home</Link>
        </Button>
      </div>
    </div>
  );
}
