import { cn } from "@/lib/cn";

type DocHtmlProps = {
  html: string;
  className?: string;
};

export function DocHtml({ html, className }: DocHtmlProps) {
  return (
    <div
      className={cn("doc-html max-w-none", className)}
      dangerouslySetInnerHTML={{ __html: html }}
    />
  );
}
