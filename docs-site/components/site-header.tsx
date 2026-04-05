import Link from "next/link";
import { FpIcon } from "@/components/fp-icon";
import { Button } from "@/components/ui/button";
import { BookOpen, Github, Home, Package } from "lucide-react";

const REPO = "https://github.com/indunet/fastproto";
const MAVEN = "https://search.maven.org/artifact/org.indunet/fastproto/4.1.0/jar";

type SiteHeaderProps = {
  variant?: "marketing" | "docs";
  title?: string;
  subtitle?: string;
};

export function SiteHeader({ variant = "marketing", title = "FastProto", subtitle }: SiteHeaderProps) {
  return (
    <header className="sticky top-0 z-50 border-b border-gray-200 bg-white/95 backdrop-blur-sm">
      <div className="mx-auto flex max-w-6xl items-center justify-between gap-4 px-5 py-3">
        <Link href="/" className="flex min-w-0 items-center gap-3 rounded-md outline-none focus-visible:ring-2 focus-visible:ring-blue-500">
          <FpIcon className="size-8" />
          <div className="min-w-0">
            {/* Clean, dark brand name — no gradient decoration */}
            <p className="truncate text-sm font-bold text-gray-900">{title}</p>
            {variant === "marketing" && subtitle ? (
              <p className="truncate text-xs text-gray-500">{subtitle}</p>
            ) : variant === "docs" ? (
              <p className="truncate text-[10px] font-semibold uppercase tracking-widest text-blue-600/70">
                Documentation
              </p>
            ) : null}
          </div>
        </Link>

        <nav className="flex items-center gap-1">
          <Button variant="ghost" size="sm" asChild>
            <Link href="/"><Home />Home</Link>
          </Button>
          <Button variant="ghost" size="sm" asChild>
            <Link href="/help/quick-start"><BookOpen />Docs</Link>
          </Button>
          <Button variant="ghost" size="sm" asChild>
            <a href={REPO} target="_blank" rel="noreferrer"><Github />GitHub</a>
          </Button>
          <Button variant="primary" size="sm" asChild>
            <a href={MAVEN} target="_blank" rel="noreferrer"><Package />Maven</a>
          </Button>
          <iframe
            title="GitHub stars"
            src="https://ghbtns.com/github-btn.html?user=indunet&repo=fastproto&type=star&count=true&size=large"
            width={140}
            height={30}
            className="ml-2 hidden overflow-hidden border-0 sm:block"
          />
        </nav>
      </div>
    </header>
  );
}
