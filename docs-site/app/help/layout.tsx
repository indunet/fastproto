import { DocSidebar } from "@/components/doc-sidebar";
import { SiteFooter } from "@/components/site-footer";
import { SiteHeader } from "@/components/site-header";

export default function HelpLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="relative z-[1] flex min-h-screen flex-col">
      <SiteHeader variant="docs" />
      <div className="mx-auto flex w-full max-w-6xl flex-1 gap-6 px-5 py-6 lg:grid lg:grid-cols-[minmax(0,260px)_minmax(0,1fr)]">
        <DocSidebar />
        <div className="min-w-0">{children}</div>
      </div>
      <SiteFooter />
    </div>
  );
}
