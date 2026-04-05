"use client";

import * as ScrollArea from "@radix-ui/react-scroll-area";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { DOC_NAV } from "@/lib/nav";
import { cn } from "@/lib/cn";

export function DocSidebar() {
  const pathname = usePathname();

  return (
    <aside className="min-w-0 lg:sticky lg:top-20 lg:self-start">
      <ScrollArea.Root className="h-full max-h-[min(42vh,320px)] w-full lg:max-h-[calc(100vh-5.5rem)]">
        <ScrollArea.Viewport className="pb-2 pr-1">
          <nav
            aria-label="Documentation"
            className="rounded-xl border border-gray-200 bg-white p-2 shadow-sm"
          >
            <p className="px-2 pb-1.5 pt-1 text-[10px] font-semibold uppercase tracking-widest text-gray-400">
              Guides
            </p>
            <ul className="space-y-0.5">
              {DOC_NAV.map((item) => {
                const href = `/help/${item.slug}`;
                const active = pathname.endsWith(`/help/${item.slug}`);
                return (
                  <li key={item.slug}>
                    <Link
                      href={href}
                      className={cn(
                        "block rounded-md px-3 py-1.5 text-sm transition-colors",
                        "border-l-2",
                        active
                          ? "border-blue-600 bg-blue-50 font-semibold text-blue-700"
                          : "border-transparent text-gray-600 hover:bg-gray-50 hover:text-gray-900",
                      )}
                    >
                      {item.title}
                    </Link>
                  </li>
                );
              })}
            </ul>
          </nav>
        </ScrollArea.Viewport>
        <ScrollArea.Scrollbar
          className="mr-0.5 flex w-1.5 touch-none select-none p-0.5"
          orientation="vertical"
        >
          <ScrollArea.Thumb className="relative flex-1 rounded-full bg-gray-300" />
        </ScrollArea.Scrollbar>
      </ScrollArea.Root>
    </aside>
  );
}
