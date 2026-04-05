import { cn } from "@/lib/cn";

export function FpIcon({ className = "" }: { className?: string }) {
  return (
    <div
      className={cn(
        "relative inline-flex size-11 shrink-0 items-center justify-center rounded-xl",
        "bg-gradient-to-br from-blue-500 to-blue-700",
        "text-white",
        "shadow-sm ring-1 ring-blue-700/20",
        className,
      )}
      aria-hidden
    >
      <span className="block text-center font-mono text-[9px] font-bold leading-tight tracking-tight">
        01
        <br />
        10
      </span>
    </div>
  );
}
