/*
 * (C) Copyright 2015-2017 by MSDK Development Team
 *
 * This software is dual-licensed under either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1 as published by the Free
 * Software Foundation
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by the Eclipse Foundation.
 */

package io.github.msdk.datamodel.impl;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.msdk.datamodel.datastore.DataPointStore;
import io.github.msdk.datamodel.featuretables.ColumnName;
import io.github.msdk.datamodel.featuretables.FeatureTable;
import io.github.msdk.datamodel.featuretables.FeatureTableColumn;
import io.github.msdk.datamodel.featuretables.Sample;
import io.github.msdk.datamodel.rawdata.MsFunction;

/**
 * Object builder
 */
public class MSDKObjectBuilder {

  /**
   * Common columns for feature tables
   */
  private static final @Nonnull SimpleFeatureTableColumn<Integer> IdFeatureTableColumn =
      new SimpleFeatureTableColumn<Integer>("Id", Integer.class, null);
  private static final @Nonnull SimpleFeatureTableColumn<Double> MzFeatureTableColumn =
      new SimpleFeatureTableColumn<Double>(ColumnName.MZ.getName(), Double.class, null);
  private static final @Nonnull SimpleFeatureTableColumn<Double> ppmFeatureTableColumn =
      new SimpleFeatureTableColumn<Double>(ColumnName.PPM.getName(), Double.class, null);
  private static final @Nonnull SimpleFeatureTableColumn<Float> RetentionTimeFeatureTableColumn =
      new SimpleFeatureTableColumn<Float>(ColumnName.RT.getName(),
          Float.class, null);
  private static final @Nonnull SimpleFeatureTableColumn<List<SimpleIonAnnotation>> IonAnnotationFeatureTableColumn =
      new SimpleFeatureTableColumn<>("Ion Annotation",
          (Class<List<SimpleIonAnnotation>>) (Class<?>) List.class, null);
  private static final @Nonnull SimpleFeatureTableColumn<Integer> ChargeFeatureTableColumn =
      new SimpleFeatureTableColumn<Integer>(ColumnName.CHARGE.getName(), Integer.class, null);

  /**
   * The number of MS functions used in a project is typically small, but each scan has to be
   * annotated with its MS function. So we take advantage of the immutable nature of MsFunction and
   * recycle the instances using this List.
   */
  private static final List<WeakReference<MsFunction>> msFunctions = new LinkedList<>();

  /**
   * Creates a new MsFunction reference.
   *
   * @param name a {@link java.lang.String} object.
   * @param msLevel a {@link java.lang.Integer} object.
   * @return a {@link io.github.msdk.datamodel.rawdata.MsFunction} object.
   */
  public static final @Nonnull MsFunction getMsFunction(@Nonnull String name,
      @Nullable Integer msLevel) {

    synchronized (msFunctions) {
      Iterator<WeakReference<MsFunction>> iter = msFunctions.iterator();
      while (iter.hasNext()) {
        WeakReference<MsFunction> ref = iter.next();
        MsFunction func = ref.get();
        if (func == null) {
          iter.remove();
          continue;
        }
        if (!func.getName().equals(name))
          continue;
        if ((func.getMsLevel() == null) && (msLevel == null))
          return func;
        if ((func.getMsLevel() != null) && (func.getMsLevel().equals(msLevel)))
          return func;
      }
      MsFunction newFunc = new SimpleMsFunction(name, msLevel);
      WeakReference<MsFunction> ref = new WeakReference<>(newFunc);
      msFunctions.add(ref);
      return newFunc;

    }
  }

  /**
   * Creates a new MsFunction reference.
   *
   * @param name a {@link java.lang.String} object.
   * @return a {@link io.github.msdk.datamodel.rawdata.MsFunction} object.
   */
  public static final @Nonnull MsFunction getMsFunction(@Nonnull String name) {
    return getMsFunction(name, null);
  }

  /**
   * Creates a new MsFunction reference.
   *
   * @param msLevel a {@link java.lang.Integer} object.
   * @return a {@link io.github.msdk.datamodel.rawdata.MsFunction} object.
   */
  public static final @Nonnull MsFunction getMsFunction(@Nullable Integer msLevel) {
    return getMsFunction(MsFunction.DEFAULT_MS_FUNCTION_NAME, msLevel);
  }


  /**
   * <p>
   * getFeatureTable.
   * </p>
   *
   * @param featureTableName a {@link java.lang.String} object.
   * @param dataPointStore a {@link io.github.msdk.datamodel.datastore.DataPointStore} object.
   * @return a {@link io.github.msdk.datamodel.featuretables.FeatureTable} object.
   */
  public static final @Nonnull FeatureTable getFeatureTable(@Nonnull String featureTableName,
      @Nonnull DataPointStore dataPointStore) {
    return new SimpleFeatureTable(featureTableName, dataPointStore);
  }

  /**
   * <p>
   * getMsSpectrum.
   * </p>
   *
   * @param mzValues an array of double.
   * @param intensityValues an array of float.
   * @param size a {@link java.lang.Integer} object.
   * @param spectrumType a {@link io.github.msdk.datamodel.msspectra.MsSpectrumType} object.
   * @return a {@link io.github.msdk.datamodel.msspectra.MsSpectrum} object.
   */
 /* public static final @Nonnull MsSpectrum getMsSpectrum(@Nonnull double mzValues[],
      @Nonnull float intensityValues[], @Nonnull Integer size,
      @Nonnull MsSpectrumType spectrumType) {
    return new SimpleMsSpectrum(mzValues, intensityValues, size, spectrumType);
  }*/

  /**
   * <p>
   * getMsScan.
   * </p>
   *
   * @param dataPointStore a {@link io.github.msdk.datamodel.datastore.DataPointStore} object.
   * @param scanNumber a {@link java.lang.Integer} object.
   * @param msFunction a {@link io.github.msdk.datamodel.rawdata.MsFunction} object.
   * @return a {@link io.github.msdk.datamodel.rawdata.MsScan} object.
   */
 /* public static final @Nonnull MsScan getMsScan(@Nonnull DataPointStore dataPointStore,
      @Nonnull Integer scanNumber, @Nonnull MsFunction msFunction) {
    return new SimpleMsScan(dataPointStore, scanNumber, msFunction);
  }*/

  /**
   * <p>
   * getChromatogram.
   * </p>
   *
   * @param dataPointStore a {@link io.github.msdk.datamodel.datastore.DataPointStore} object.
   * @param chromatogramNumber a {@link java.lang.Integer} object.
   * @param chromatogramType a {@link io.github.msdk.datamodel.chromatograms.ChromatogramType}
   *        object.
   * @param separationType a {@link io.github.msdk.datamodel.rawdata.SeparationType} object.
   * @return a {@link io.github.msdk.datamodel.chromatograms.Chromatogram} object.
   */
  /* public static final @Nonnull Chromatogram getChromatogram(@Nonnull DataPointStore dataPointStore,
      @Nonnull Integer chromatogramNumber, @Nonnull ChromatogramType chromatogramType,
      @Nonnull SeparationType separationType) {
    return new SimpleChromatogram(dataPointStore, chromatogramNumber, chromatogramType,
        separationType);
  }*/





  /**
   * Creates a new FeatureTableColumn instance.
   *
   * @return new SimpleFeatureTableColumn
   * @param columnName a {@link io.github.msdk.datamodel.featuretables.ColumnName} object.
   * @param sample a {@link io.github.msdk.datamodel.featuretables.Sample} object.
   * @param <DataType> a DataType object.
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static @Nonnull <DataType> FeatureTableColumn<DataType> getFeatureTableColumn(
      @Nonnull ColumnName columnName, @Nullable Sample sample) {
    return new SimpleFeatureTableColumn<DataType>(columnName.getName(),
        (Class) columnName.getDataTypeClass(), sample);
  }

  /**
   * <p>
   * getFeatureTableColumn.
   * </p>
   *
   * @param name a {@link java.lang.String} object.
   * @param dataTypeClass a {@link java.lang.Class} object.
   * @param sample a {@link io.github.msdk.datamodel.featuretables.Sample} object.
   * @param <DataType> a DataType object.
   * @return a {@link io.github.msdk.datamodel.featuretables.FeatureTableColumn} object.
   */
  public static @Nonnull <DataType> FeatureTableColumn<DataType> getFeatureTableColumn(
      @Nonnull String name, @Nonnull Class<DataType> dataTypeClass, @Nullable Sample sample) {
    return new SimpleFeatureTableColumn<DataType>(name, dataTypeClass, sample);
  }

  /**
   * Creates a new FeatureTableRow instance.
   *
   * @return new SimpleFeatureTableRow
   * @param featureTable a {@link io.github.msdk.datamodel.featuretables.FeatureTable} object.
   * @param rowId a int.
   */
  public static @Nonnull SimpleFeatureTableRow getFeatureTableRow(
      @Nonnull FeatureTable featureTable, int rowId) {
    return new SimpleFeatureTableRow(featureTable, rowId);
  }

  /**
   * <p>
   * getMzFeatureTableColumn.
   * </p>
   *
   * @return a {@link io.github.msdk.datamodel.featuretables.FeatureTableColumn} object.
   */
  public static @Nonnull FeatureTableColumn<Double> getMzFeatureTableColumn() {
    return MzFeatureTableColumn;
  }

  /**
   * <p>
   * Getter for the field <code>ppmFeatureTableColumn</code>.
   * </p>
   *
   * @return a {@link io.github.msdk.datamodel.featuretables.FeatureTableColumn} object.
   */
  public static @Nonnull FeatureTableColumn<Double> getPpmFeatureTableColumn() {
    return ppmFeatureTableColumn;
  }

  /**
   * <p>
   * getChromatographyInfoFeatureTableColumn.
   * </p>
   *
   * @return a {@link io.github.msdk.datamodel.featuretables.FeatureTableColumn} object.
   */
  public static @Nonnull FeatureTableColumn<Float> getRetentionTimeFeatureTableColumn() {
    return RetentionTimeFeatureTableColumn;
  }

  /**
   * <p>
   * getIdFeatureTableColumn.
   * </p>
   *
   * @return a {@link io.github.msdk.datamodel.featuretables.FeatureTableColumn} object.
   */
  public static @Nonnull FeatureTableColumn<Integer> getIdFeatureTableColumn() {
    return IdFeatureTableColumn;
  }

  /**
   * <p>
   * getIonAnnotationFeatureTableColumn.
   * </p>
   *
   * @return a {@link io.github.msdk.datamodel.featuretables.FeatureTableColumn} object.
   */
  public static @Nonnull FeatureTableColumn<List<SimpleIonAnnotation>> getIonAnnotationFeatureTableColumn() {
    return IonAnnotationFeatureTableColumn;
  }

  /**
   * <p>
   * getChargeFeatureTableColumn.
   * </p>
   *
   * @return a {@link io.github.msdk.datamodel.featuretables.FeatureTableColumn} object.
   */
  public static @Nonnull FeatureTableColumn<Integer> getChargeFeatureTableColumn() {
    return ChargeFeatureTableColumn;
  }





}
