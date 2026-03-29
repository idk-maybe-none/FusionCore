package dev.allofus.fusioncore;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.DocumentsProvider;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class FusionDocumentProvider extends DocumentsProvider {

    private File baseDir;

    @Override
    public boolean onCreate() {
        try {
            baseDir = Objects.requireNonNull(getContext()).getFilesDir();
        } catch (Exception e) {
            return false;
        }
        return baseDir != null;
    }

    @Override
    public ParcelFileDescriptor openDocument(String documentId, String mode, @Nullable CancellationSignal signal) throws FileNotFoundException {
        return ParcelFileDescriptor.open(new File(baseDir, documentId), ParcelFileDescriptor.parseMode(mode));
    }

    @Override
    public Cursor queryChildDocuments(String parentDocumentId, String[] projection, String sortOrder) throws FileNotFoundException {
        final var result = new MatrixCursor(projection);
        final var parentFile = new File(baseDir, parentDocumentId);
        try {
            if (parentFile.isDirectory()) {
                for (var child : Objects.requireNonNull(parentFile.listFiles())) {
                    includeFile(result, child);
                }
            }
        } catch (Throwable t) {
            // Ignore exceptions and return an empty cursor
        }
        return result;
    }

    @Override
    public Cursor queryDocument(String documentId, String[] projection) throws FileNotFoundException {
        final var result = new MatrixCursor(projection);
        includeFile(result, new File(documentId));
        return result;
    }

    @Override
    public Cursor queryRoots(String[] projection) throws FileNotFoundException {
        final var result = new MatrixCursor(projection);
        final var row = result.newRow();
        row.add(DocumentsContract.Root.COLUMN_ROOT_ID, "root");
        row.add(DocumentsContract.Root.COLUMN_DOCUMENT_ID, baseDir.getAbsolutePath());
        row.add(DocumentsContract.Root.COLUMN_TITLE, "Fusion Core");
        row.add(DocumentsContract.Root.COLUMN_FLAGS, DocumentsContract.Root.FLAG_SUPPORTS_CREATE);
        row.add(DocumentsContract.Root.COLUMN_ICON, R.mipmap.app_icon);
        return result;
    }

    private void includeFile(MatrixCursor cursor, File file) {
        final var row = cursor.newRow();
        row.add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, file.getAbsoluteFile());
        row.add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, file.getName());
        row.add(DocumentsContract.Document.COLUMN_SIZE, file.length());
        if (file.isDirectory()) {
            row.add(DocumentsContract.Document.COLUMN_MIME_TYPE, DocumentsContract.Document.MIME_TYPE_DIR);
        } else {
            row.add(DocumentsContract.Document.COLUMN_MIME_TYPE, "*/*");
        }
        row.add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, file.lastModified());
        row.add(DocumentsContract.Document.COLUMN_FLAGS,
                DocumentsContract.Document.FLAG_SUPPORTS_WRITE |
                        DocumentsContract.Document.FLAG_SUPPORTS_DELETE);
    }
}
