package com.example.applicationone;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {

  @Override
  public boolean onCreate() {
    return false;
  }

  @Nullable
  @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
      @Nullable String[] selectionArgs, @Nullable String sortOrder) {

    String[] columns = {"UltrasoundImages"};
    MatrixCursor matrixCursor = new MatrixCursor(columns);

    Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cloud_icon);
    Bitmap[] bitmaps = {bitmap};
    matrixCursor.addRow(bitmaps);

    Bitmap bitmap2 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.sun_icon);
    Bitmap[] bitmaps2 = {bitmap2};
    matrixCursor.addRow(bitmaps2);

    return matrixCursor;
  }

  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    return null;
  }

  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    return null;
  }

  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection,
      @Nullable String[] selectionArgs) {
    return 0;
  }

  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
      @Nullable String[] selectionArgs) {
    return 0;
  }
}
